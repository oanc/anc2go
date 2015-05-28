var table = document.getElementById("corpDirTable");
var checkboxes = table.getElementsByTagName('input');
var selectedProc = document.getElementById("procField");

$('#annotation-tabs a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
    selectedProc.value = $(this).text();
});

function toggleAll(bool) {
	for(var i=0; i<checkboxes.length; i++) {
		if (checkboxes[i].type == 'checkbox') {
            checkboxes[i].checked = !checkboxes[i].checked;
        }
	}
}