
//Проба функции по повороту коробля в вертикальное положение

function onClickRotate(myObject) {
    var myOffset=myObject.offset();
    if (iFdragged) return;
    var xPos = myOffset.left;
    var yPos = myOffset.top;
    var cellStart=getElsAt(yPos, xPos);
    if (cellStart.length==0) return;
    
    cellObject.fromId(cellStart.attr("id"));
    var shipLength=Model.getWidth(cellObject.x, cellObject.y);
    if (shipLength==1) return;
    var savedId=Model.getId(cellObject.x, cellObject.y);
    var savedPos=Model.getPos(cellObject.x, cellObject.y);
    var desiredPos;

    if (savedPos==positioN.HOR) desiredPos=positioN.VERT;
        else desiredPos=positioN.HOR;

    var resultChk=Model.validateShip(cellObject.x, cellObject.y, desiredPos, shipLength, savedId);

    if (resultChk) {
        Model.deleteShip(cellObject.x, cellObject.y, shipLength);
        Model.poseShip(cellObject.x, cellObject.y, desiredPos, shipLength, savedId);
        myObject.remove();
        insertDiv(xPos, yPos, shipLength, desiredPos);
        Model.updateViev();
    }
}
