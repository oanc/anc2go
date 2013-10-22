<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="ANC2Go" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico') }" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <!--  
        <div id="grailsLogo"><a href="http://anc.org"><img src="${resource(dir:'images',file:'anc-logo2.jpg')}" alt="ANC" border="0" /></a></div>
        <div class="data">
        	<span>15em</span>
        	<span id="drop-file-menu-width">15em</span>
        </div>
        <div id="menu-bar" class="nav">
        	<a href="#" id="file-menu">File</a>
        	<a href="#" id="edit-menu">Edit</a>
        	<a href="#" id="help-menu">Help</a>
        </div>
        <div id="drop-file-menu" class="drop-menu">
        	<a href="/ANC2Go/file/open">Open</a>
        	<a href="/ANC2Go/file/close">Close</a>
        	<a href="/ANC2Go/file/save">Save</a>
        </div>
        <div id="drop-edit-menu" class="drop-menu">
        	<a href="/ANC2Go/edit/cut">Cut</a>
        	<a href="/ANC2Go/edit/copy">Copy</a>
        	<a href="/ANC2Go/edit/paste">Paste</a>
        </div>
        <div id="drop-help-menu" class="drop-menu">
        	<a href="/ANC2Go/help/about">About</a>
        </div>
        -->
        <g:layoutBody />
        <div class="clear"></div>
        <div class="block">
        <anc:copyright/>
        </div>
    </body>
</html>