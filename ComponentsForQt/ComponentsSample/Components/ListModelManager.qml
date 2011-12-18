import QtQuick 1.0

WorkerScript {
    id: _root
    source: "./ListModelManagerScript.js"   // スレッド処理をするスクリプトファイルの指定

    property variant model: null
    property int count: (model === null) ? 0 : model.count

    // Workerスレッドからの通信イベント
    onMessage: {
        console.debug("finish thread : " + messageObject.result);
    }

    // データチェック
    function isOk(object){
        if((model === null) || (object === null)
                || (object === undefined)){
            return false;
        }else{
            return true;
        }
    }

    // 追加する
    function append(object){
        if(!isOk(object)){
            // nop
        }else{
            sendMessage({"model": model
                            , "type": "append"
                            , "object": object
                               });
        }
    }

    // クリアする
    function clear(){
        if(!isOk(true)){
            // nop
        }else{
            sendMessage({"model": model
                            , "type": "clear"
                            });
        }
    }

    // 取得する
    function get(index){
        if(!isOk(true)){
            return null;
        }else{
            return model.get(index);
        }
    }

    // 挿入する
    function insert(index, object){
        if(!isOk(object)){
            // nop
        }else{
            sendMessage({"model": model
                            , "type": "insert"
                            , "index": index
                            , "object": object
                               });
        }
    }

    // 移動する
    function move(from, to, n){
        if(!isOk(true)){
            // nop
        }else{
            model.move(from, to, n);
//            sendMessage({"model": model
//                            , "type": "move"
//                            , "from": from
//                            , "to": to
//                            , "n": n
//                               });
        }
    }

    // 削除する
    function remove(index){
        if(!isOk(true)){
            // nop
        }else{
            model.remove(index);
//            sendMessage({"model": model
//                            , "type": "remove"
//                            , "index": index
//                               });
        }
    }

    // 内容を変更する
    function set(index, object){
        if(!isOk(object)){
            // nop
        }else{
            sendMessage({"model": model
                            , "type": "set"
                            , "index": index
                            , "object": object
                               });
        }
    }

    // プロパティの変更
    function setProperty(index, property, value){
        if(!isOk(value)){
            // nop
        }else{
            sendMessage({"model": model
                            , "type": "setProperty"
                            , "index": index
                            , "property": property
                            , "value": value
                               });
        }
    }

    // 同期
    function sync(){
        sendMessage({"model": model, "type": "sync"});
    }
}
