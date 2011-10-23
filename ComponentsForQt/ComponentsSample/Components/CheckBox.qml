import QtQuick 1.0

MouseArea {
    id: _root
    width: _checkBase.width + _text.width + _text.anchors.leftMargin * 2
    height: (_checkBase.height > _text.height) ? _checkBase.height : _text.height

    property bool checked: false                        // チェック状態
    property alias text: _text.text                     // 表示するメッセージ
    property alias color: _text.color                   // 色
    property alias fontPointSize: _text.font.pointSize  // フォントサイズ
    property int checkSize: 20                          // チェックのサイズ

    // クリックイベント
    onClicked: {
        checked = !checked;
    }

    // チェック自体のサイズ変更
    onCheckSizeChanged: {
        if(checkSize < 10){
            checkSize = 10;
        }
    }

    // チェックマーク
    Rectangle {
        id: _checkBase
        width: checkSize
        height: checkSize
        radius: 5
        anchors.left: parent.left
        anchors.verticalCenter: parent.verticalCenter

        // ベースのグラデ
        gradient: Gradient {
            GradientStop {
                id: _grad1
                position: 0
                color: "#555555"
            }
            GradientStop {
                id: _grad2
                position: 1
                color: "#333333"
            }
        }

        // On/Offするところ
        Rectangle {
            id: _check
            width: checkSize - 4
            height: checkSize - 4
            radius: 5
            anchors.centerIn: parent
            // グラデ（OFF時の色）
            gradient: Gradient {
                GradientStop {
                    id: _checkGrad1
                    position: 0
                    color: "#000000"
                }
                GradientStop {
                    id: _checkGrad2
                    position: 1
                    color: "#555555"
                }
            }
        }
    }

    // テキスト
    Text {
        id: _text
        anchors.left: _checkBase.right
        anchors.leftMargin: 5
        anchors.verticalCenter: parent.verticalCenter

        text: "text"
    }


    // 状態管理
    states: [
        State {
            name: "On"
            when: checked
            PropertyChanges {
                target: _checkGrad1
                color: "#B5E61D"
            }
            PropertyChanges {
                target: _checkGrad2
                color: "#637E0E"
            }
        }
    ]
}
