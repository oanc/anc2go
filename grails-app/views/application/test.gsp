 <html>
	<head>
		<title>ANC2Go</title>
		<anc:dojo theme="claro"/>
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
			dojo.require("dijit.form.RadioButton")
			
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

			function corpusClick(event)
			{
				console.log(event.title + " clicked.")
				var hidden = dijit.byId("selectedCorpus")
				if (hidden)
				{
					console.log("Setting selected corpus to " + event.title)
					hidden.attr("value", event.title)
				}
				else
				{
					console.log("Could not find the hidden control for the selected corpus.")
				}
				if (event.title == "MASC")
				{
					console.log("Showing MASC tab")
					dojo.query(".OANC").forEach(hide)
					dojo.query(".MASC").forEach(show)
				}
				else
				{
					console.log("Showing OANC tab")
					dojo.query(".MASC").forEach(hide)
					dojo.query(".OANC").forEach(show)
				}
			}

			function processorClick(event)
			{
				var hidden = dijit.byId("selectedProcessor")
				if (hidden)
				{
					console.log("Processor is " + event.title)
					hidden.attr("value", event.title)
				}
				else
				{
					console.log("Could not find the hidden control for the selected processor.")
				}
			}

			function err(message)
			{
				errorDialog.attr("content", message)
				errorDialog.show()
			}
			
			var errorDialog 
			function init()
			{
				dojo.query(".OANC").forEach(hide)
				var dialog = dijit.byId('error-dialog')
				if (dialog)
				{
					console.log("Showing dialog.")
					dialog.show()
				}
				
				var panel = dijit.byId("corpusPanel")
				if (panel)
				{
					console.log("Connecting corpus panel to onClick")
					dojo.connect(panel, "selectChild", corpusClick)
				}
				panel = dijit.byId("processorPanel")
				if (panel)
				{
					console.log("Connecting processor panel to onClick")
					dojo.connect(panel, "selectChild", processorClick)
				}
				errorDialog = new dijit.Dialog()
				errorDialog.title = "ERROR"
			}
			dojo.addOnLoad(init)
			
		</script>		
	</head>
	<body class="claro">
	
		<g:if test="${flash.message}">
			<anc:dialog title="Info" id="error-dialog">			
				<p class="error">${flash.message}</p>
			</anc:dialog>
		</g:if>
		<div id="appLayout">
			<anc:form action="submit" method="post">
				<script type="dojo/method" event="onSubmit" args="event">
					if (!this.isValid())
					{
						dojo.stopEvent(event)
						alert("Missing email field.")
						return false
					}
					var email1 = dijit.byId("email1")
					var email2 = dijit.byId("email2")
					if (!email1)
					{
						err("Email1 not found on form.")
						return false
					}
					if (!email2)
					{
						err("Email2 not found on form.")
						return false
					}
					if (email1.value != email2.value)
					{
						err("Email addesses do not match")
						return false
					}
					var processor = dojo.byId('selectedProcessor')
					var corpus = dojo.byId('selectedCorpus')
					console.log("Selected corpus is " + corpus.value)
					console.log(corpus)
					var count = 0
					dojo.query(".DIR_" + corpus.value + ".dijitChecked").forEach(function(checkbox){
						++count
					})
					if (count == 0)
					{
						err("No directories have been selected for processing.")
						return false
					}
					return true
				</script>
				<anc:hidden id="selectedCorpus" name="selectedCorpus" value="MASC"/>
				<anc:hidden id="selectedProcessor" name="selectedProcessor" value="XML"/>
								
			<anc:border design="headline"  class="mainPanel">
				<anc:content region="top" class="topPanel" splitter="true">
					<img src="${resource(dir:'images',file:'anc-logo5.png')}" class="anc-logo" id="anc-logo">
					<table class="anc-table" id="anc-top-table">
						<tr>
							<td>Enter your email:</td>
							<td><anc:email id="email1" name="email1" value="${email}"/></td>
							<td rowspan="2" class="anc-desc">
								<p class="anc-note"><b>Beta version 3.0b1.</b><br/>This software is still undergoing
								development and testing. Some features may not work.</p>
								<p>Since processing may take some time you will be sent an
								email with a link to your download when it is ready.</p>
							</td>
						</tr>
						<tr>
							<td>Confirm email:</td>							
							<td><anc:email id="email2" name="email2" value="${email}"/></td>
						</tr>
					</table>
				</anc:content>
				
				<anc:tabbed region="left" class="leftPanel" splitter="true" id="corpusPanel">
					<g:each in="${corpora.asList().sort()}" var="corpus">					
						<anc:content title="${corpus.name}" class="anc-tab" id="corpus-${corpus.name}" >
							<h2>${corpus.toString()}</h2>
							<table class="list">
								<g:each in="${corpus.directories.asList().sort()}" var="dir">
									<tr>
										<td>
											<anc:checkbox 
												id="${corpus.name}_DIR_${dir.name}" 
												name="F_${corpus.name}_DIR_${dir.name}" 
												class="DIR_${corpus.name}" 
												checked="false"/>
										</td>
										<td>${dir.textClass} - ${dir.name}</td>
									</tr>
								</g:each>
							</table>
						</anc:content>
					</g:each> 
				</anc:tabbed>
				<anc:tabbed region="center" class="centerPanel" id="processorPanel">
					<anc:content title="XML" class="scroll-fix">
					
						<h3>Notes</h3>
						<p>Not all combinations of annotations result in well-formed XML files.
              For example, both the logical and Penn TreeBank annotations define hierarchies (trees)
              of annotations, and it is likely that a combination of these two annotation types will
              result in hierarchies that overlap and cannot be properly nested using XML tags. The XML
              processor will attempt to resolve simple problems with overlapping annotations using the
              overlap mode chosen below, but it may not be possible to fully resolve all such instances.</p>
              			<h3>Configuration</h3>
              			<div class="OANC">
              			<p>OANC</p>
						<table class='list'>
							<tr>
								<td><anc:radio id="xml-gate-tokens" name="xml-tokens" value="xml-gate-tokens"/></td>
								<td colspan="2">GATE Tokens</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-logical" name="xml-logical" checked='false'/></td>
								<td>Logical</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-s" name="xml-s" checked='false'/></td>
								<td>Sentences</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-ne" name="xml-ne" checked='false'/></td>
								<td>Named entities</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-nc" name="xml-nc" checked='false'/></td>
								<td>Noun chunks</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-vc" name="xml-vc" checked='false'/></td>
								<td>Named entities</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-ev" name="xml-vc" checked='false'/></td>
								<td>Events</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-cb" name="xml-vc" checked='false'/></td>
								<td>Committed Belief</td>
							</tr>
						</table>
						<table class='list'>
							<tr>
								<td><anc:radio id='xml-ptb-tokens' name='xml-tokens' value='xml-ptb-tokens'/></td>
								<td colspan='2'>Penn TreeBank Tokens w/POS</td>
							</tr>
							<tr>
								<td>&nbsp</td>
								<td><anc:checkbox id='xml-ptb-syntax' name='xml-ptn-syntax' checked='false'/></td>
								<td>Penn TreeBank syntax</td>
							</tr>
						</table>
						<table class='list'>
							<tr>
								<td><anc:radio id='xml-fn-tokens' name='xml-tokens' value='xml-fn-tokens'/></td>
								<td colspan='2'>Framenet Tokens w/POS</td>
							</tr>
							<tr>
								<td>&nbsp</td>
								<td><anc:checkbox id='xml-fn-syntax' name='xml-fn-syntax' checked='false'/></td>
								<td>Framenet syntax</td>
							</tr>
						</table>
						</div>
						<div class="MASC">
						<p>MASC</p>
						<table class='list'>
							<tr>
								<td><anc:radio id="xml-gate-tokens" name="xml-tokens" value="xml-gate-tokens"/></td>
								<td colspan="2">GATE Tokens</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-logical" name="xml-logical" checked='false'/></td>
								<td>Logical</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-s" name="xml-s" checked='false'/></td>
								<td>Sentences</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-ne" name="xml-ne" checked='false'/></td>
								<td>Named entities</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-nc" name="xml-nc" checked='false'/></td>
								<td>Noun chunks</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-vc" name="xml-vc" checked='false'/></td>
								<td>Named entities</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-ev" name="xml-vc" checked='false'/></td>
								<td>Events</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><anc:checkbox id="xml-cb" name="xml-vc" checked='false'/></td>
								<td>Committed Belief</td>
							</tr>
						</table>
						<table class='list'>
							<tr>
								<td><anc:radio id='xml-ptb-tokens' name='xml-tokens' value='xml-ptb-tokens'/></td>
								<td colspan='2'>Penn TreeBank Tokens w/POS</td>
							</tr>
							<tr>
								<td>&nbsp</td>
								<td><anc:checkbox id='xml-ptb-syntax' name='xml-ptn-syntax' checked='false'/></td>
								<td>Penn TreeBank syntax</td>
							</tr>
						</table>
						<table class='list'>
							<tr>
								<td><anc:radio id='xml-fn-tokens' name='xml-tokens' value='xml-fn-tokens'/></td>
								<td colspan='2'>Framenet Tokens w/POS</td>
							</tr>
							<tr>
								<td>&nbsp</td>
								<td><anc:checkbox id='xml-fn-syntax' name='xml-fn-syntax' checked='false'/></td>
								<td>Framenet syntax</td>
							</tr>
						</table>
						</div>
					</anc:content>
					
					<anc:content title="NLTK">
						<table>
							<tr>
								<td><anc:radio id="nltk-gate" name="nltk"/></td>
								<td>GATE Tokens with POS</td>
							<tr>
							<tr>
								<td><anc:radio id="nltk-ptb" name="nltk"/></td>
								<td>PTB Tokens with POS</td>
							<tr>
							<tr>
								<td><anc:radio id="nltk-fn" name="nltk"/></td>
								<td>Framenet Tokens with POS</td>
							<tr>
						</table>
					</anc:content>

					<anc:content title="CONLL">
					</anc:content>

					<anc:content title="Token w/POS">
					</anc:content>

					<anc:content title="UIMA CAS">
					</anc:content>
				</anc:tabbed>
				<anc:content region="bottom">
					<div class="center">
						<g:submitButton name="submit" value="Select annotations"/>
					</div>
				</anc:content>
			</anc:border>			
			</anc:form>
		</div>
	</body>
</html>
