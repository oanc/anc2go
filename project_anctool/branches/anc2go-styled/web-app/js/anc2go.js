var table = document.getElementById("corpDirTable");
var checkboxes = table.getElementsByTagName('input');

function selectAll(bool) {
	console.log(bool)
	for(var i=0; i<checkboxes.length; i++) {
		if (checkboxes[i].type == 'checkbox') {
            checkboxes[i].checked = bool;
        }
	}
}