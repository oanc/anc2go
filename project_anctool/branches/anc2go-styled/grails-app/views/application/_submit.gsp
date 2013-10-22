<html>
	<head>
		<title>Form results</title>	
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
			<anc:content region="top" class="topPanel">
				<p>Processor: ${processor}<br/>
				Corpus: ${corpus}</p>
			</anc:content>
			<anc:tabbed region="center" class="centerPanel">
				<anc:content title="Directories">
					<ul>
						<g:each var="dir" in="${directories}">
							<li>${dir}</li>
						</g:each>
					</ul>
				</anc:content>
				<anc:content title="Annotations">
					<ul>
						<g:each var="type" in="${types}">
							<li>${type}</li>
						</g:each>			
					</ul>
				</anc:content>
				<anc:content title="Options">
					<g:if test="${options.size() > 0}">
						<h2>Options</h2>
						<table class="data">
							<tr>
								<th>Name</th>
								<th>Value</th>
							</tr>
							<g:each var="opt" in="${options}" status="i">
								<tr class="${i % 2 == 0 ? 'even' : 'odd'}"}>
									<td>${opt.key}</td>
									<td>${opt.value}
								</tr>
							</g:each>
						</table>
					</g:if>
					<g:else>
						<p>No options available for this processor.</p>
					</g:else>
				</anc:content>
				<anc:content title="Params">
					<table class="data">
						<tr>
							<th>Name</th>
							<th>Value</th>
						</tr>
						<g:each var="param" in="${params}" status="i">
							<tr class="${i % 2 == 0 ? 'even' : 'odd'}"}>
								<td>${param.key}</td>
								<td>${param.value}</td>
							</tr>
						</g:each>
					</table>
				</anc:content>
			</anc:tabbed>
		</anc:border>				
	</div>

	</body>
</html>