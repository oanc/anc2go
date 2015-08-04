var checkTable = document.getElementById('corpDirTable');
var checkboxes = checkTable.getElementsByTagName('input');
var selectedProc = document.getElementById('procField');

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

function validateEmail() {
    var email1 = document.getElementById('email-input').value;
    var email2 = document.getElementById('email-confirm-input').value;
    var match = email1.toLowerCase().trim() == email2.toLowerCase().trim();

    if(!match) {
        var errorField = document.getElementById('error-field');
        errorField.innerHTML = "Please make sure the email addresses match.";
    }

    return match;
}