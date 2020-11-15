/**
 * wehappy项目中用到的所有websocket操作都通过该类封装
 * @param url ws地址
 * @param protocols ws的自定义协议
 * @constructor
 */
function WehappyWebSocket(url, protocols) {
    // 判断当前浏览器是否支持websocket
    if (!window.WebSocket) {
        alert("当前浏览器不支持websocket")
    }

    this.webSocket = protocols ? new WebSocket(url, protocols) : new WebSocket(url)
    this.webSocket.binaryType = "arraybuffer";
}

// 全局自增消息Id
let id = 1;
// 接收推送的wsUrl
let receiveWsUrl = "ws://localhost:9001/receive";
// 发送消息的wsUrl
let sendWsUrl = "ws://localhost:9001/chat";
// token,用于身份认证
let token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTMyNzQ5OTYwNDAzOTcwODY3MywiZXhwIjoxNjA1NDIxOTY5LCJ1c2VyX25hbWUiOiJsb2xsaXBvcCIsImp0aSI6ImNlMGZmNjQ0LWQwNmMtNDZiYi04NGQzLTBmY2NmMmU5MWQ4NiIsImNsaWVudF9pZCI6InBvcnRhbCIsInNjb3BlIjpbImFsbCJdfQ.WhpxtkSa5Aw0hh-9kjXIutfdjw-2TpOGGCRhE0IZlPuYc_nYeqvhh4Bw4u19Y9gDQYybl4Vlp2Ui_tatgDuYrhFHIGtQRGknUcczi-5Cafp6sT12t958wqwZyqDjOSIBFCWIxvbwH7Qg4h7elTwZGlHQF6Psp-1j_yGMzD4k8dc4i7zbhFfCTB5qfNzpWVDU12KFUR-5N44ncqYaCd8N_VwtoN75inoj7Y011n5-McrKBC7BlGgGidQ4aOn1IgU-efTLBNDen09yx1144Z-iU3tYUpCHFJFq77MLX9PaS-6IExXS8k7UTIu_t9c_ggw-BjjDWc8PcMxkVxw_XHL_Pw";
// 接收推送消息的websocket对象
let receiveWebSocket = new WehappyWebSocket(receiveWsUrl, [token])
// 发送消息的websocket对象
let sendWebSocket = new WehappyWebSocket(sendWsUrl, [token])

/**
 * 接受到消息(感知到服务端发来消息)
 * @param ev
 */
receiveWebSocket.webSocket.onmessage = function (ev) {
    let message;
    if (ev.data instanceof ArrayBuffer) {
        message = proto.Message.deserializeBinary(ev.data);
    } else {
        message = ev.data
    }
    const rt = document.getElementById("responseText");
    rt.value += "\n sequence: " + message.getSequence();
    rt.value += "\n id: " + message.getId();
    rt.value += "\n type: " + message.getMessagetype();
    rt.value += "\n to: " + message.getTo();
    rt.value += "\n content: " + JSON.stringify(getPushMessageContent(message.getPushmessage()));
}

/**
 * 相当于连接开启(感知到连接开启)
 * @param ev
 */
receiveWebSocket.webSocket.onopen = function (ev) {
    const rt = document.getElementById("responseText");
    rt.value = "连接开启了.."
}

/**
 * 相当于连接关闭(感知到连接关闭)
 * @param ev
 */
receiveWebSocket.webSocket.onclose = function (ev) {
    console.log(ev)
    const rt = document.getElementById("responseText");
    rt.value = rt.value + "\n" + "连接关闭了.."
}

/**
 * 获取具体消息内容
 * @param message 消息
 * @returns {string|?proto.ChatMessage|?proto.ResponseMessage}
 */
function getMessageContent(message) {
    if (message.getMessagetype() === proto.MessageType.HEART_BRAT_MESSAGE) {
        return 'HEART_BEAT';
    }

    if (message.getMessagetype() === proto.MessageType.RESPONSE_MESSAGE) {
        return message.getResponsemessage();
    }

    if (message.getMessagetype() === proto.MessageType.SINGLE_MESSAGE || proto.MessageType.GROUP_MESSAGE) {
        return message.getChatmessage();
    }
}

/**
 * 解析推送消息内容
 * @param pushMessage 推送消息
 * @returns {{time: Date, contentType: !proto.ContentType, content: string}}
 */
function getPushMessageContent(pushMessage) {
    return {
        time: new Date(pushMessage.getTime()),
        contentType: pushMessage.getContenttype(),
        content: pushMessage.getContent(),
    }
}

/**
 * 发送私聊消息
 * @param to 私聊用户Id
 * @param contentType 消息内容类型
 * @param content 消息内容
 */
function sendSingleChatMessage(to, contentType, content) {
    sendMessage(buildMessage(proto.MessageType.SINGLE_MESSAGE, to, buildChatMessage(contentType, to, content)))
}

/**
 * 发送群聊消息
 * @param groupId 群组Id
 * @param contentType 消息内容类型
 * @param content 消息内容
 */
function sendGroupChatMessage(groupId, contentType, content) {
    sendMessage(buildMessage(proto.MessageType.GROUP_MESSAGE, 0, buildChatMessage(contentType, groupId, content)))
}

/**
 * 构造聊天消息对象
 * @param contentType 消息内容类型
 * @param to 发送给的用户或群组Id
 * @param content 消息内容
 * @returns {proto.ChatMessage}
 */
function buildChatMessage(contentType, to, content) {
    let chatMessage = new proto.ChatMessage();
    chatMessage.setTo(to);
    chatMessage.setContent(content);
    chatMessage.setContenttype(contentType);
    return chatMessage;
}

/**
 * 构造消息对象
 * @param messageType 消息类型
 * @param to 发送给的用户Id
 * @param msg 具体消息
 * @returns {proto.Message}
 */
function buildMessage(messageType, to, msg) {
    let message = new proto.Message();
    message.setTo(to);
    message.setMessagetype(messageType);
    message.setChatmessage(msg);
    return message;
}

/**
 * 发送消息到服务器
 * @param message
 */
function sendMessage(message) {
    // 先判断socket是否创建好
    if (!sendWebSocket) {
        return;
    }

    if (sendWebSocket.webSocket.readyState === WebSocket.OPEN) {
        // 通过socket 发送消息
        message.setId(id++);
        sendWebSocket.webSocket.send(message.serializeBinary());
    } else {
        alert("连接没有开启");
    }
}
