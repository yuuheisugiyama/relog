import QtQuick 1.0

Row{
    id: _root
    height: 30
    spacing: 3

    property int value: 50              // 値
    property int min: 0                 // 最小値
    property int max: 100               // 最大値
    property int step: 10               // 増減量
    property int stepAccelerate: 50     // 増減量（加速ボタン）
    property bool accelerate: false     // 加速ボタンあり？

    property int buttonWidth: 50        // ボタンの横幅


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
        width: buttonWidth
        height: _root.height
        text:"<<"
        visible: accelerate
        onClicked: {
            updateValue(-1 * stepAccelerate);
        }
    }
    Button{
        width: buttonWidth
        height: _root.height
        text:"<"
        onClicked: {
            updateValue(-1 * step);
        }
    }
    Text {
        id: _number
        width: 50
        height: _root.height
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        text: value
        onTextChanged: {
            if(text.length === 0){
            }else{
                value = parseInt(text);
            }
        }
    }
//    TextBox{
//        id: _number
//        width: 50
//        text: value
//        onTextChanged: {
//            if(text.length === 0){
//            }else{
//                value = parseInt(text);
//            }
//        }
//    }
    Button{
        width: buttonWidth
        height: _root.height
        text:">"
        onClicked: {
            updateValue(step);
        }
    }
    Button{
        width: buttonWidth
        height: _root.height
        text:">>"
        visible: accelerate
        onClicked: {
            updateValue(stepAccelerate);
        }
    }
}
