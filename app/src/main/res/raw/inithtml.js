window.JSObject.addTitle(document.title);
var objs = document.getElementsByTagName("img");
for(var i=0; i<objs.length; i++) {
    window.JSObject.addImage(objs[i].src);
    objs[i].onclick = function(){
        window.JSObject.openImageInWeb(this.src);
    }
}