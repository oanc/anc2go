<html>
<head>
	<meta name="layout" content="main" />
	<title>Select Annotations</title>
</head>
<body>
	<div class="block">
		<h1>Annotations</h1>
		<table>
			<tr><td>Corpus</td><td>${corpus}</td></tr>
			<tr><td>Processor</td><td>${processor}</td></tr>
		</table>
		<h1>Select</h1>
		<p>Please select the annotation type(s) to be processed.</p>
		<table>
			<g:each in="${result}" var="t" status="i">
				<tr><td>${i}</td><td>${t}</td></tr>
			</g:each>
		</table>
		<anc:home/>
	</div>
</body>
</html>