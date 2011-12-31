import QtQuick 1.0

ListView {
    id: _root
    x: 0
    y: 0
    width: parent.width
    height: parent.height
    clip: true

    property bool loading: (count < 1)

    property bool alwaysViewing: false                      // スクロールバーを常に表示するか
    property alias scrollBarVisible: _scrollBarBase.visible // 表示するか
    property alias scrollBarBaseColor: _scrollBarBase.color // ベース色
    property alias scrollBarColor: _scrollBar.color         // 色
    property real scrollBarOpacity: 0.8                     // 透過率

    property real density: 1.0

    Rectangle {
        id: _scrollBarBase
        width: 25 * _root.density
        anchors.top: parent.top
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        anchors.margins: 2
        color: "#22ffffff"
        radius: 2 * _root.density
        opacity: 0.01
        visible: (visibleArea.heightRatio < 1.0) | alwaysViewing

        Rectangle {
            id: _scrollBar
            anchors.horizontalCenter: parent.horizontalCenter
            y: parent.height * visibleArea.yPosition
            width: parent.width * 0.1
            radius: width * 0.5
            height: parent.height * visibleArea.heightRatio
            color: "#ffffff"
        }

        MouseArea{
            anchors.fill: parent
            onClicked: {
                var per = mouse.y / height;
                var index = 0;
                if(per < 0.1){
                    // 一番上へ
                    index = 0;
                }else if(per > 0.9){
                    // 一番下へ
                    index = count - 1;
                }else{
                    // 中間
                    var h = height * 0.1;
                    index = Math.floor((count) * ((mouse.y - h) / (height - 2 * h)));
                }
                positionViewAt(index, ListView.Beginning);
            }
        }
    }


    //読込中のくるくる画像
    Image {
        anchors.centerIn: parent
        source: "./images/spinner.png"

        NumberAnimation on rotation {
            from: 0
            to: -360
            duration: 1500
            loops: Animation.Infinite
            running: loading
        }
        visible: loading
    }

    // 選択を変更した時にスクロールバーを表示する。
    onCurrentIndexChanged: {
        if(!movingVertically && !alwaysViewing){
            if(state !== "Visible"){
                state = "Visible";
                _scrollBarVisibleTimer.start();
            }else if(state === "Visible"){
                _scrollBarVisibleTimer.restart();
            }
        }
    }

    // 選択を変更したときのスクロールバーを消すためのタイマー
    Timer{
        id: _scrollBarVisibleTimer
        interval: 500
        repeat: false
        triggeredOnStart: false
        onTriggered:{
            state = "";
        }
    }

    // キー操作
    Keys.onPressed: {
        switch(event.key){
        case Qt.Key_Home:
            // 一番上へ
            positionViewAtBeginEnd(true);
            break;
        case Qt.Key_End:
            // 一番下へ
            positionViewAtBeginEnd(false);
            break;
        case Qt.Key_PageUp:
            // 上へ飛ぶ
            positionViewAtPageUpDown(false);
            break;
        case Qt.Key_PageDown:
            // 下へ飛ぶ
            positionViewAtPageUpDown(true);
            break;
        }
    }

    /////////////////////////////////////////////
    // 表示位置をコントロールする

    // 表示位置を最初と最後
    function positionViewAtBeginEnd(is_begin){
        if(is_begin){
            positionViewAtIndex(0, ListView.Beginning);
            currentIndex = 0;
        }else{
            positionViewAtIndex(count - 1, ListView.Beginning);
            currentIndex = count - 1;
        }
    }

    // 表示位置をページ移動
    function positionViewAtPageUpDown(is_down){
        // ヘッダとフッタの高さを考慮する
        var top = indexAt(1, contentY);
        var bottom = indexAt(1, contentY + height);
        if(top < 0){
            top = 0;
        }
        if(bottom <= top){
            bottom = count - 1;
        }
        if(is_down){
            // 下へ
            positionViewAt(currentIndex + (bottom - top), ListView.End);
        }else{
            // 上へ
            positionViewAt(currentIndex - (bottom - top), ListView.Beginning);
        }
    }

    // 表示位置を任意の位置へ移動
    function positionViewAt(index, mode){
        if(index < 0){
            index = 0;
        }else if(index >= count){
            index = count - 1;
        }
        positionViewAtIndex(index, mode);
        currentIndex = index;
    }

    // ステータス管理
    states: [
        State {
            name: "Visible"
            when: movingVertically | alwaysViewing
            PropertyChanges {
                target: _scrollBarBase
                opacity: scrollBarOpacity
            }
        }
    ]

    //アニメーション
    transitions:
        Transition {
            PropertyAnimation {
                easing.type: Easing.OutCubic
                target: _scrollBarBase
                properties: "opacity"
                duration: 300
            }
        }

}
