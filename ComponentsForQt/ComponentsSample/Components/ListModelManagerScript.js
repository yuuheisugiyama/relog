var _countMax = 1000;
var _limitCutTop = true;

WorkerScript.onMessage = function(message) {
    console.debug("start worker script");

    if(message.model === null){
        // nop
    }else{

        switch(message.type){
        case "append":
            checkLimit(message.model);
            message.model.append(message.object);
            break;
        case "clear":
            message.model.clear();
            break;
        case "insert":
            checkLimit(message.model);
            message.model.insert(message.index, message.object);
            break;
        case "move":
            message.model.move(message.from, message.to, message.n);
            break;
        case "remove":
            message.model.remove(message.index);
            break;
        case "set":
            message.model.set(message.index, message.object);
            break;
        case "setProperty":
            message.model.setProperty(message.index, message.property, message.value);
            break;
        case "setLimit":
            _countMax = message.max;
            _limitCutTop = message.top;
            break;
        default:
            break;
        }

        message.model.sync();
    }

    // メインスレッドへの応答
    WorkerScript.sendMessage({ "result": "true" })
}

function checkLimit(model){
    console.debug("limit?=" + model.count);
    while(model.count >= _countMax){
        if(_limitCutTop){
            model.remove(0);
        }else{
            model.remove(model.count - 1);
        }
    }
}
