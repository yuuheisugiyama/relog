import QtQuick 1.0
import "Components"

Rectangle {
    width: 360
    height: 360

    Column{
        anchors.fill: parent
        anchors.margins: 5
        spacing: 5

//        // サンプル1
//        CheckBox {
//            text: "CheckBox1"
//            checked: true

//            // 変更イベント
//            onCheckedChanged: {
//                console.debug(text + ":" + checked);
//            }
//        }

//        // サンプル2
//        CheckBox {
//            text: "CheckBox2"
//            color: "#dd0055"
//            checked: false

//            // 変更イベント
//            onCheckedChanged: {
//                console.debug(text + ":" + checked);
//            }
//        }

        // ボタン
        Button {
            width: 200
            height: 50
            text: "デザインてすと"
        }

        // グループボックス
        GroupBox{
            text: "Radio box test"
            radius: 5
            height: _radio.height + textHeight * 1.5

            // ラジオボックス
            RadioBox{
                id: _radio
                currentIndex: 1     // 選択の初期位置変更
                anchors.top: parent.top
                anchors.margins: parent.textHeight * 1.2
                anchors.left: parent.left
                anchors.leftMargin: 5

                // 項目の設定
                model: RadioBoxModel{
                }

                // 項目が変更された時のイベント
                // こっちが先に呼ばれる（まだキーは変更されてない）
                onCurrentIndexChanged: {
                    console.debug("onCurrentIndexChanged::"
                                  + "index=" + currentIndex
                                  + ", text=" + items[currentIndex].text
                                  + ", text=" + getCurrentItemText()
                                  + ", count=" + getItemCount()
                                  + ", key=" + currentKey);
                }

                // 項目が変更されてキーが変更された時のイベント
                // こっちが後に呼ばれる
                onCurrentKeyChanged: {
                    console.debug("onKeyChanged::"
                                  + "index=" + currentIndex
                                  + ", text=" + items[currentIndex].text
                                  + ", text=" + getCurrentItemText()
                                  + ", count=" + getItemCount()
                                  + ", key=" + currentKey);
                }
            }

        }
    }

}
