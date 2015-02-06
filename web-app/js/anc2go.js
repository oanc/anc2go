var table = document.getElementById("corpDirTable");
var checkboxes = table.getElementsByTagName('input');

$('#annotation-tabs a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
});

function toggleAll(bool) {
	for(var i=0; i<checkboxes.length; i++) {
		if (checkboxes[i].type == 'checkbox') {
            checkboxes[i].checked = !checkboxes[i].checked;
        }
	}
}