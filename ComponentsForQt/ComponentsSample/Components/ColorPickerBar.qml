import QtQuick 1.0

Rectangle{
    id: _root
    width: parent.width
    height: parent.height
    clip: true

    // 自分自身の色の強さ
    property real value: 0
    // 表示色用のパラメータ
    property real viewRedMin: 0.0
    property real viewRedMax: 1.0
    property real viewGreenMin: 0.0
    property real viewGreenMax: 1.0
    property real viewBlueMin: 0.0
    property real viewBlueMax: 1.0
    property real viewAlphaMin: 0.0
    property real viewAlphaMax: 1.0

    // 透けたときに見える背景
    Image{
        anchors.fill: parent
        fillMode: Image.Tile
        source: "./images/alpha_background.png"
    }
    // 色見本のグラデーション
    Rectangle{
        anchors.fill: parent
        radius: parent.radius
        border.color: parent.border.color
        border.width: parent.border.width
        gradient: Gradient {
            GradientStop {position: 0.0;  color: Qt.rgba(viewRedMin, viewGreenMin, viewBlueMin, viewAlphaMin)}
            GradientStop {position: 1.0;  color: Qt.rgba(viewRedMax, viewGreenMax, viewBlueMax, viewAlphaMax)}
        }
    }
    // ポインタ
    Rectangle{
        anchors.left: parent.left
        anchors.leftMargin: parent.border.width
        anchors.right: parent.right
        anchors.rightMargin: parent.border.width
        y: parent.height * value
        height: 2
        color: "#bbbbbb"
        Rectangle{
            anchors.bottom: parent.bottom
            x: 0
            width: parent.width
            height: 1
            color: "#333333"
        }
    }

    // 色変更用のクリックイベント
    MouseArea{
        anchors.fill: parent
        onMousePositionChanged: {
            // 自分の色を変更する
            _root.value = mouse.y / height;
            if(_root.value > 1.0){
                _root.value = 1.0;
            }else if(_root.value < 0.0){
                _root.value = 0.0;
            }
        }
    }
}
