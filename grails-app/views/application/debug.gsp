<html>
	<head>
		<title>Debug Values</title>	
		<anc:dojo theme="claro"/>
		<script type="text/javascript">
			dojo.require("dijit.layout.BorderContainer")
			dojo.require("dijit.layout.TabContainer")
			dojo.require("dijit.layout.ContentPane")
			dojo.require("dijit.TitlePane")
			dojo.require("dijit.Dialog")
		</script>
		<link type="text/css" href="${resource(dir:'css',file:'main.css')}" />
	</head>
	<body class="claro">
		<div class="appLayout">
			<anc:border class="mainPanel">
				<anc:content region="center" class="centerPanel">
					<h1>Global PATH Constants</h1>
					<table border='1'>
						<tr><th>Name</th><th>Value</th></tr>
						<tr>
							<td>Cache</td>
							<td>${cache}</td>
						</tr>
						<tr>
							<td>Downloads</td>
							<td>${downloads}</td>
						</tr>
						<tr>
							<td>Work</td>
							<td>${work}</td>
						</tr>
					</table>
					<h1>Global Mail Constants</h1>
					<table border='1'>
						<tr><th>Name</th><th>Value</th></tr>
						<tr>
							<td>Default address</td>
							<td>${org.anc.togo.constants.Globals.SMTP.DEFAULT_ADDRESS}</td>
						</tr>
						<tr>
							<td>Server</td>
							<td>${org.anc.togo.constants.Globals.SMTP.HOST}</td>
						</tr>
						<tr>
							<td>User</td>
							<td>${org.anc.togo.constants.Globals.SMTP.USER}</td>
						</tr>
					</table>
				</anc:content>
			</anc:border>
		</div>
	</body>
</html>