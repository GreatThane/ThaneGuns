function zoomFunction(zoomLevels, index) {
    var returnString = "§a§lZoom Level§8: ";
    for (var i = 0; i < zoomLevels.length; i++) {
        var prefix = "§7";
        if (i === index) prefix = "§a";
        if (i === zoomLevels.length -1) returnString += prefix + zoomLevels[i] + " §8| ";
    }
    return returnString;
}