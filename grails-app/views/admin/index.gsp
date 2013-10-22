<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>ANC2Go Administration</title>
</head>
<body>
  <div class="body">
  	<div class="block">
  	<h1>ANC2Go Admin</h1>
  	<ul>
  		<li><g:link action="user">User admin</g:link></li>
  		<li><g:link controller="corpus" action="index">Corpus administration</g:link></li>
  		<li><g:link controller="annotationType" action="index">Type administration</g:link></li>
  		<li><g:link controller="processor" action="index">Processor administration</g:link></li>
  	</ul>
  	</div>
  	<anc:home />
  </div>
</body>
</html>