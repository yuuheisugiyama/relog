import QtQuick 1.0

Item {
    id: _root
    width: _items.width
    height: _items.height

    property int currentKey: 0              // 現在のアイテムのmodelで指定されたキー（読込み専用）
    property int currentIndex: 0            // 現在の選択アイテムのインデックス
    property alias items: _items.children   // checkboxのリスト
    property alias itemSpacing: _items.spacing  // アイテム同士の間隔

    property alias model: _itemsRepeater.model

    // 読込み完了時の処理
    Component.onCompleted: {
        // 初期位置を設定する
        if(currentIndex < 0 || currentIndex >= getItemCount()){
            currentIndex = 0;
        }
        _items.children[currentIndex].checked = true;
        currentKey = _itemsRepeater.model.get(currentIndex)._key;
    }

    // 含まれるcheckboxの数
    function getItemCount(){
        // Repeaterの分を引く
        return _items.children.length - 1;
    }

    // 現在選択されているアイテムのテキスト
    function getCurrentItemText(){
        return _items.children[currentIndex].text;
    }

    // 自分以外のチェックを外す
    function itemClicked(index){
        for(var i=0; i< getItemCount(); i++){
            if(i == index){
                // 自分自身はなにもしない
            }else{
                // 自分以外のチェックを外す
                _items.children[i].checked = false;
            }
        }
    }

    // 実際の内容
    Column{
        id: _items
        spacing: 5

        // リピータでお手軽配置
        // modelは外から指定する
        // _key は一意にすること
        // _text は表示したい文字列
        Repeater {
            id: _itemsRepeater

            delegate: CheckBox{
                id: _item
                text: _text
                property int key: _key

                onClicked: {
                    //インデックスなどを更新する
                    currentIndex = index;
                    currentKey = key;
                    // 自分以外のチェックを外す
                    itemClicked(index);
                }
            }
        }
    }

}
