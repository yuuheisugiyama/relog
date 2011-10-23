import QtQuick 1.0

Item {
    id: _root
    width: _items.width
    height: _items.height

    property int currentIndex: 0            // 現在の選択アイテムのインデックス
    property alias items: _items.children   // checkboxのリスト
    property alias itemSpacing: _items.spacing  // アイテム同士の間隔


    // 読込み完了時の処理
    Component.onCompleted: {
        // 初期位置を設定する
        if(currentIndex < 0 || currentIndex >= getItemCount()){
            currentIndex = 0;
        }
        _items.children[currentIndex].checked = true;
    }

    // 含まれるcheckboxの数
    function getItemCount(){
        return _items.children.length;
    }

    // 現在選択されているアイテムのテキスト
    function getCurrentItemText(){
        return _items.children[currentIndex].text;
    }

    // 自分以外のチェックを外す
    function itemClicked(item){
        for(var i=0; i< _items.children.length; i++){
            if(_items.children[i].text === item.text){
                // 自分自身は何もしない
                currentIndex = i;
            }else{
                // 自分以外のチェックを外す
                _items.children[i].checked = false;
            }
        }
    }

    Column{
        id: _items
        spacing: 5

        Repeater {
        }

        // チェックボックス０
        CheckBox{
            id: _item0
            text: "item 0"
            onClicked: {
                itemClicked(_item0);
            }
        }

        // チェックボックス１
        CheckBox{
            id: _item1
            text: "item 1"
            onClicked: {
                itemClicked(_item1);
            }
        }

        // このあたりにチェックボックスを置いていく
        // idは必ず設定してonClickedで
        // itemClicked(自分のID)で呼ぶこと

        // チェックボックスN
        CheckBox{
            id: _itemN
            text: "item N"
            onClicked: {
                itemClicked(_itemN);
            }
        }
    }

}
