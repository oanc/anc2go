<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="ANC2Go" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'anc.css')}" />
        <link rel="stylesheet" href="${resource(dir:'js', file:'/dojo-1.9.1/dijit/themes/tundra/tundra.css') }"/>       
        <link rel="stylesheet" href="${resource(dir:'js', file:'/dojo-1.9.1/dojo/resources/dojo.css') }"/>
        <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" />
        <g:javascript src="/dojo-1.9.1/dojo/dojo.js" djConfig="parseOnLoad:true"> </g:javascript>
		<script type="text/javascript">
			dojo.require("dijit.layout.BorderContainer")
			dojo.require("dijit.layout.TabContainer")
			dojo.require("dijit.layout.ContentPane")
			dojo.require("dijit.TitlePane")
			dojo.require("dijit.Dialog")
			dojo.require("dijit.Tooltip")
			dojo.require("dojo.data.ItemFileReadStore");
			dojo.require("dijit.Tree");
			dojo.require("dijit.tree.ForestStoreModel")
			dojo.require("dijit.tree.TreeStoreModel")
			dojo.require("dijit.form.Form")
			dojo.require("dijit.form.CheckBox")
			dojo.require("dijit.form.TextBox")
			dojo.require("dijit.form.ValidationTextBox")
			dojo.require("digit.form.RadioButton")
			
			function hide(node)
			{
				//dojo.fadeOut({node:node}).play()
				node.style.visibility = 'hidden'
			}

			function show(node)
			{
				//dojo.fadeIn({node:node}).play()
				node.style.visibility = 'visible'
			}
		</script>
		<g:layoutHead/>
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>

        <g:layoutBody />
        <div class="clear"></div>
        <div class="block">
        <anc:copyright/>
        </div>
    </body>
</html>