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
			
			function selectAll()
			{
				var table = document.getElementById('corpusPanel')
				var checkboxes = table.getElementsByTagName('input')
				var len = checkboxes.length
				for (var i = 0; i < len; i++) {
					dijit.byId(checkboxes[i].id).set('checked', true)
				}
			}
			function clearAll()
			{
				var table = document.getElementById('corpusPanel')
				var checkboxes = table.getElementsByTagName('input')
				var len = checkboxes.length
				for (var i = 0; i < len; i++) {
					dijit.byId(checkboxes[i].id).set('checked', false)
				}

			}
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
					// Check that directories are selected
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
				
				<!-- The left hand panel contains tabs for the available corpora and a list 
				     of check boxes to allow the user to select portions of the corpus. -->
				<anc:tabbed region="left" class="leftPanel" splitter="true" id="corpusPanel">
					<g:each in="${corpora.asList().sort()}" var="corpus">					
						<anc:content title="${corpus.name}" class="anc-tab" id="corpus-${corpus.name}" >
							<h2>${corpus.toString()}</h2>
							<table class="list" id="corpDirTable">
								<g:each in="${corpus.directories.asList().sort()}" var="dir">
									<tr>
										<td>
											<anc:checkbox 
												id="DIR_${corpus.cid}_${dir.did}" 
												name="F_${corpus.cid}_${dir.did}_dir" 
												class="DIR_${corpus.cid}" 
												checked="false"/>
										</td>
										<td>${dir.textClass} - ${dir.name}</td>
									</tr>
								</g:each>
							</table>
							<button type="button" onClick="selectAll()">Select All</button>
							<button type="button" onClick="clearAll()">Clear All</button>
						</anc:content>
					</g:each> 
				</anc:tabbed>
				<anc:tabbed region="center" class="centerPanel" id="processorPanel">
					<g:each var="descriptor" in="${descriptors}">
						<anc:content title="${descriptor.name}" class="anc-tab">
							<g:each var="cd" in="${descriptor.corpora}">
								<div class="${cd.name}">
									<g:if test="${cd.notes}">
										<h3>Notes</h3>
										<g:each var="p" in="${cd.notes}">
											<p>${p}</p>
										</g:each>
									</g:if>
									<g:if test="${cd.tokens}">
										<h3>Configuration </h3>
										<g:each var="token" in="${cd.tokens}" status="i">

											<table class="list">
												<tr>

													<%-- GATE --%>

													<g:if
														test="${token.description.contains('GATE')}">
													<td><anc:radio
															id="${descriptor.name}_${cd.name}_${token.name}"
															name="F_${cd.name}_${descriptor.name}_tokens"
															value="${token.name}" 
															checked="checked"/></td>
														<td>
															${token.description}
														</td>

														<tr>
															<anc:content title="annotations">
																<g:each var="a" in="${cd.annotations}">
																	<%-- this is terrible, but otherwise layout gets off --%>
																	<g:if test="${!a.description.contains('syntax') && !a.description.contains('FrameNet')}">
																		<tr>
																			<td></td>
																			<td><anc:checkbox
																					id="ann_${descriptor.name}_${cd.name}_${a.name}"
																					name="F_${cd.name}_${descriptor.name}_${a.name}"/>
																					${a.description}</td>
																		</tr>
																	</g:if>
																</g:each>
															</anc:content>
														</tr>
													</g:if>

													<%-- PENN TREEBANK --%>
													
													<g:elseif
														test="${token.description.contains('Penn TreeBank Tokens')}">
														<td><anc:radio
															id="${descriptor.name}_${cd.name}_${token.name}"
															name="F_${cd.name}_${descriptor.name}_tokens"
															value="${token.name}" />
														</td>
														<td>
															${token.description}
														</td>

														<g:each var="a" in="${cd.annotations}">
															<g:if test="${a.description.contains('syntax')}">
																<tr>
																	<td></td>
																	<td><anc:checkbox
																			id="ann_${descriptor.name}_${cd.name}_${a.name}"
																			name="F_${cd.name}_${descriptor.name}_${a.name}"
																			class="anc-button" />
																			${a.description}</td>
																</tr>
															</g:if>
														</g:each>

													</g:elseif>
													
													<%-- FRAMENET --%>
													
													<g:elseif
														test="${token.description.contains('FrameNet Tokens')}">
														<td><anc:radio
															id="${descriptor.name}_${cd.name}_${token.name}"
															name="F_${cd.name}_${descriptor.name}_tokens"
															value="${token.name}" />
														</td>
														<td>
															${token.description}
														</td>
														
														<g:each var="a" in="${cd.annotations}">
															<g:if test="${a.description.contains('FrameNet')}">
																<tr>
																	<td></td>
																	<td><anc:checkbox
																			id="ann_${descriptor.name}_${cd.name}_${a.name}"
																			name="F_${cd.name}_${descriptor.name}_${a.name}"
																			class="anc-button" />
																			${a.description}</td>
																</tr>
															</g:if>
														</g:each>
														
													</g:elseif>
													
													<%-- NO TOKENS --%>
													<%-- 
													<g:elseif
														test="${token.description.contains('No Tokens')}">
														<td>
															${token.description}
														</td>
														
													</g:elseif>
													--%>
												</tr>

											</table>
											
											<br>
											
										</g:each>

									</g:if>
									<%--<g:if test="${cd.options}">
										<g:each var="opt" in="${cd.options}">
											<h3>${opt.title}</h3>
											<table>
												<g:each var="item" in="${opt.items}">
													<g:if test="${item.checked}">
														<tr>
														<td><anc:radio id="${descriptor.name}_${cd.name}_option_${item.label}" name="F_${cd.name}_${descriptor.name}_option_${opt.name}" value="${item.label}" checked="true"/></td>
														<td>${item.label} (Default)</td>
														</tr>
													</g:if>
													<g:else>
														<tr>
														<td><anc:radio id="${descriptor.name}_${cd.name}_option_${item.label}" name="F_${cd.name}_${descriptor.name}_option_${opt.name}" value="${item.label}"/></td>
														<td>${item.label}</td>
														</tr>
													</g:else>
												</g:each>
											</table>
										</g:each>
									</g:if>--%>
								</div>
							</g:each>
						</anc:content>
					</g:each>
				</anc:tabbed>
				<anc:content region="bottom">
					<div class="center">
						<g:submitButton name="submit" value="Start"/>
					</div>
				</anc:content>
			</anc:border>			
			</anc:form>
		</div>
	</body>
</html>
