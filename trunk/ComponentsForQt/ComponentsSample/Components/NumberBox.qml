import QtQuick 1.0

Row{
    id: _root
    height: _button.height
    spacing: 3

    property int value: 50              // 値
    property int min: 0                 // 最小値
    property int max: 100               // 最大値
    property int step: 10               // 増減量
    property int stepAccelerate: 50     // 増減量（加速ボタン）
    property bool accelerate: false     // 加速ボタンあり？

    property int buttonWidth: 50        // ボタンの横幅
    property int textWidth: 50          // テキストの横幅

    property real density: 1.0

    // 値を更新する
    function updateValue(step){
        var temp = value + step;
        if(temp < min){
            value = min;
        }else if(temp > max){
            value = max;
        }else{
            value = temp;
        }
    }

    Button{
        width: _button.width
        text:"<<"
        font.pointSize: 12 * _root.density
        visible: accelerate
        onClicked: {
            updateValue(-1 * stepAccelerate);
        }
    }
    Button{
        id: _button
        text:"  <  "
        font.pointSize: 12 * _root.density
        onClicked: {
            updateValue(-1 * step);
        }
    }
    Rectangle{
        width: (_number.paintedWidth < textWidth) ? textWidth : _number.paintedWidth
        height: _root.height
        border.color: "#dddddd"
        border.width: 2

        Text {
            id: _number
            anchors.centerIn: parent
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            text: value
            font.pixelSize: parent.height * 0.8// * _root.density
            onTextChanged: {
                if(text.length === 0){
                }else{
                    value = parseInt(text);
                }
            }
        }
    }
    Button{
        text:"  >  "
        font.pointSize: 12 * _root.density
        onClicked: {
            updateValue(step);
        }
    }
    Button{
        width: _button.width
        text:">>"
        font.pointSize: 12 * _root.density
        visible: accelerate
        onClicked: {
            updateValue(stepAccelerate);
        }
    }
}
