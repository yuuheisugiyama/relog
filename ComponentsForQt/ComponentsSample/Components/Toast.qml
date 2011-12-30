import QtQuick 1.0

Rectangle {
    id: _root
    width: _text.paintedWidth * 1.2// _text.width + _text.font.pixelSize
    height: _text.paintedHeight * 1.5//_text.height + _text.font.pixelSize / 2
    color: "#222222"
    border.color: "#ffffff"
    border.width: 1
    opacity: 0

    property alias text: _text.text
    property alias textColor: _text.color
    property alias pointSize: _text.font.pointSize
    property alias interval: _viewTimer.interval


    //表示する
    function show(message){
        _text.text = message;
        if(_viewTimer.running){
            _viewTimer.stop();
        }
        _viewTimer.start();
    }

    //消す
    function hide(){
        _viewTimer.stop();
    }

    //自動で消すためのタイマー
    Timer {
        id: _viewTimer
        interval: 2000  //タイマー間隔
        running: false  //動いてない
        repeat: false   //リピートなし
        //トリガー動作
        onTriggered:{
        }
    }

    //表示するテキスト
    Text {
        id: _text
//        width: _root.parent.width * 0.6 - _text.font.pixelSize
        anchors.centerIn: parent
        color: "#ffffff"
        text: ""
        horizontalAlignment: Text.AlignHCenter
        wrapMode: Text.WordWrap
        font.pointSize: 14

    }

    //状態
    states: [
        State {
            name: "visible"
            when: _viewTimer.running
            PropertyChanges {
                target: _root
                opacity: 1.0
            }
        }
    ]

    //アニメーション
    transitions:
        Transition {
            PropertyAnimation {
                easing.type: Easing.OutCubic
                target: _root
                properties: "opacity"
                duration: 500
            }
        }

}
