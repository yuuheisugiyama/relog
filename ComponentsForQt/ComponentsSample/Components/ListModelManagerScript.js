WorkerScript.onMessage = function(message) {
    console.debug("start worker script");

    if(message.model === null){
        // nop
    }else{
        switch(message.type){
        case "append":
            message.model.append(message.object);
            break;
        case "clear":
            message.model.clear();
            break;
        case "insert":
            message.model.insert(message.index, message.object);
            break;
        case "move":
            message.model.move(message.from, message.to, message.n);
            break;
        case "set":
            message.model.set(message.index, message.object);
            break;
        case "setProperty":
            message.model.setProperty(message.index, message.property, message.value);
            break;
        default:
            break;
        }

        message.model.sync();
    }

    // メインスレッドへの応答
    WorkerScript.sendMessage({ "result": "true" })
}
