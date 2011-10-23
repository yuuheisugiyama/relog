import QtQuick 1.0

MouseArea {
    id: _root
    width: 100//_label.width * 1.2
    height: _label.font.pixelSize * 1.2

    property alias text: _label.text
    property alias font: _label.font

    signal pressKey(int key)

    Rectangle {
        id: _background
        anchors.fill: parent
        border.color: "#2855a8"
        border.width: 2
        radius: parent.height * 0.1
        smooth: true
        gradient: Gradient {
            GradientStop {id: _gradBg1;  position: 0.0;  color: "#2582e1"}
            GradientStop {id: _gradBg2;  position: 0.5;  color: "#2582e1"}
            GradientStop {id: _gradBg3;  position: 1.0;  color: "#135393"}
        }

        Rectangle {
            height: parent.height / 2
            anchors.top: parent.top
            anchors.left: parent.left
            anchors.right: parent.right
            anchors.margins: 1
            radius: _background.radius
            gradient: Gradient {
                GradientStop {id:_grad1;  position: 0.0;  color: "#66FFFFFF";}
                GradientStop {id:_grad2;  position: 1.0;  color: "#11FFFFFF";}
            }
            smooth: true
        }
    }


    Text {
        id: _label
        anchors.fill: parent
        text: "ボタン"
        color: "white"
        font.bold: true
        verticalAlignment: Text.AlignVCenter
        horizontalAlignment: Text.AlignHCenter
    }

    //キーイベント
    Keys.onPressed: {
        pressKey(event.key);
    }

    states: [
        State {
            name: "pressed"
            when: _root.pressed
            PropertyChanges { target: _gradBg1; color: "#e42d4b" }
            PropertyChanges { target: _gradBg2; color: "#e42d4b" }
            PropertyChanges { target: _gradBg3; color: "#931a2e" }
            PropertyChanges { target: _label; anchors.leftMargin: 3 }
            PropertyChanges { target: _label; anchors.topMargin: 3 }
        },
        State {
            name: "forcus"
            when: _root.focus
            PropertyChanges { target: _gradBg1; color: "#e89223" }
            PropertyChanges { target: _gradBg2; color: "#e89223" }
            PropertyChanges { target: _gradBg3; color: "#935e1a" }
        }
    ]
}
