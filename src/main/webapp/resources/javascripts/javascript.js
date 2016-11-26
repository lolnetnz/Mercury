function ScrollDown() {
	var body = $("body");
	$("html, body").animate({ scrollTop: document.getElementById("content").offsetHeight}, 1500);
	return;
}

function ScrollUp() {
	$("html, body").animate({ scrollTop: 0}, 1500);
	return;
}