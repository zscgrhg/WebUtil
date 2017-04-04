var $;

if (!$) {
    alert("load jquery")
    var element = document.createElement("script");
    element.src = "https://code.jquery.com/jquery-1.11.3.js";
    document.body.appendChild(element)
} else {
    alert($("body").html())
}