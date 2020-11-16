// 全局自增消息Id
let id = 1;
// 接收推送的wsUrl
let receiveWsUrl = "ws://localhost:9901/receive";
// 发送消息的wsUrl
let sendWsUrl = "ws://localhost:9901/chat";
// token, 用于身份认证
let subProtocols = ["Bearer",];

// 接收推送消息的websocket对象
let receiveWebSocket;
// 发送消息的websocket对象
let sendWebSocket;

let chatTypeInput;
let toIdInput;
let messageInput;
let responseInput;
let sendMessageButton;
let clearResponseButton;
let userId1Input;
let userId2Input;
let loginButton;

window.onload = () => {
    chatTypeInput = document.getElementById("chatType");
    toIdInput = document.getElementById("toId");
    messageInput = document.getElementById("message");
    responseInput = document.getElementById("response");
    sendMessageButton = document.getElementById("sendMessage");
    clearResponseButton = document.getElementById("clearResponse")
    loginButton = document.getElementById("login");
    userId1Input = document.getElementById("userId1");
    userId2Input = document.getElementById("userId2");
    sendMessageButton.onclick = function () {
        if (chatTypeInput.value) {
            sendSingleChatMessage(toIdInput.value, proto.ContentType.TEXT, messageInput.value);
        } else {
            sendGroupChatMessage(toIdInput.value, proto.ContentType.TEXT, messageInput.value);
        }
    }

    clearResponseButton.onclick = function () {
        responseInput.value = '';
    }

    loginButton.onclick = function () {
        subProtocols.push(userId2Input.checked ? userId1Input.value : userId2Input.value);
        receiveWebSocket = new WehappyWebSocket(receiveWsUrl, subProtocols);
        sendWebSocket = new WehappyWebSocket(sendWsUrl, subProtocols);

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
            responseInput.value += "\n sequence: " + message.getSequence();
            responseInput.value += "\n id: " + message.getId();
            responseInput.value += "\n type: " + message.getMessagetype();
            responseInput.value += "\n to: " + message.getTo();
            responseInput.value += "\n content: " + JSON.stringify(getPushMessageContent(message.getPushmessage()));
        }

        /**
         * 相当于连接开启(感知到连接开启)
         * @param ev
         */
        receiveWebSocket.webSocket.onopen = function (ev) {
            responseInput.value = "连接开启了.."
        }

        /**
         * 相当于连接关闭(感知到连接关闭)
         * @param ev
         */
        receiveWebSocket.webSocket.onclose = function (ev) {
            console.log(ev)
            responseInput.value += "\n" + "连接关闭了.."
        }
    }
}

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
function sendGroupChatMessage(to, contentType, content) {
    sendMessage(buildMessage(proto.MessageType.GROUP_MESSAGE, 0, buildChatMessage(contentType, to, content)))
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
function sendMessage(message, sendWebSocket) {
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
