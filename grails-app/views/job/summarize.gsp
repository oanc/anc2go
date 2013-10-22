<html>
	<head>
		<title>Job summary</title>	
		<anc:dojo theme="claro"/>
		<script type="text/javascript">
			dojo.require("dijit.layout.BorderContainer")
			dojo.require("dijit.layout.TabContainer")
			dojo.require("dijit.layout.ContentPane")
			dojo.require("dijit.TitlePane")
			dojo.require("dijit.Dialog")
		</script>
		<g:resource dir="css" file="main.css"/>
	</head>
	<body class="claro">
		<div class="appLayout">
			<anc:border class="mainPanel">
				<anc:content region="center" class="centerPanel">
				<h1>Jobs</h1>
				<table>
					<tr><th>ID</th><th>Key</th><th>Path</th><th>Ready</th><th>Date</th></tr>
					<g:each var="job" in="${jobs}">
						<tr>
							<td>${job.id}</td>
							<td>${job.key}</td>
							<td>${job.path}</td>
							<td>${job.ready}</td>
							<td>${job.dateCreated}</td>
						</tr>
					</g:each>
				</table>
				<h1>Pending Jobs</h1>
				<table>
					<tr><th>Job</th><th>Email</th><th>Date</th></tr>
					<g:each var="job" in="${pending}">
						<tr>
							<td>${job.job.id}</td>
							<td>${job.email}</td>
							<td>${job.dateCreated}</td>
						</tr>
					</g:each>
				</table>
				</anc:content>
			</anc:border>
		</div>
	</body>
</html>