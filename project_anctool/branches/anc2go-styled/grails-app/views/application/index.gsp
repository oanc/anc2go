<html>
<head>
	<title>ANC2Go Multi</title>
	<link rel="stylesheet" type="text/css" href=<g:resource dir="css" file="anc2go.css" /> />
</head>
<body>
	<%-- menu code --%>
	<div id="nav">
		<div id="nav_content">
			<a href="http://www.anc.org" id="logo"></a>
			<div class="menu">
				<ul>
					<li><a href="http://www.anc.org/" alt="Home">home</a></li>
					<li class=""><a href="http://www.anc.org/about/" alt="About the ANC Project">about</a>
						<ul class='children'>
							<li class=""><a href="http://www.anc.org/about/people/" alt="People">people</a></li>
							<li class=""><a href="http://www.anc.org/about/contact/" alt="Contact Us">contact</a></li>
							<li class=""><a href="http://www.anc.org/about/updates/" alt="Updates">updates</a></li>
							<li class=""><a href="http://www.anc.org/about/publications/" alt="Publications">publications</a></li>
							<li class=""><a href="http://www.anc.org/about/anc-in-the-news/" alt="anc in the news">anc in the news</a></li>
						</ul>
					</li>
					<li class=""><a href="http://www.anc.org/data/" alt="data">data</a>
						<ul class='children'>
							<li class=""><a href="http://www.anc.org/data/oanc/" alt="Open ANC">oanc</a></li>
							<li class=""><a href="http://www.anc.org/data/masc/" alt="MASC">masc</a></li>
							<li class=""><a href="http://www.anc.org/data/anc-second-release/" alt="ANC Second Release">second release</a></li>
						</ul>
					</li>
					<li class=""><a href="http://www.anc.org/contribute/" alt="contribute">contribute</a>
						<ul class='children'>
							<li class=""><a href="http://www.anc.org/contribute/texts/" alt="Contribute Texts">texts</a></li>
							<li class=""><a href="http://www.anc.org/contribute/contribute/" alt="Contribute Annotations">data and annotations</a></li>
						</ul>
					</li>
					<li class="current-page-ancestor current-page-parent"><a href="http://www.anc.org/software/" alt="tools">tools</a>
						<ul class='children'>
							<li class=""><a href="http://www.anc.org/software/anc-tool/" alt="ANC Tool">ANC Tool</a></li>
							<li class="current-page-item"><a href="http://www.anc.org/software/anc2go/" alt="ANC2Go">ANC2Go</a></li>
							<li class=""><a href="http://www.anc.org/software/gate-tools/" alt="GATE Tools">GATE Tools</a></li>
							<li class=""><a href="http://www.anc.org/software/uimautils/" alt="UIMA Tools">UIMA Tools</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<wp:frame>
	<div id="subnav">
		<ul>
			<li class=""><a href="http://www.anc.org/software/anc-tool/" alt="ANC Tool">ANC Tool</a></li>
			<li class="current-page-item"><a href="http://www.anc.org/software/anc2go/" alt="ANC2Go">ANC2Go</a></li>
			<li class=""><a href="http://www.anc.org/software/gate-tools/" alt="GATE Tools">GATE Tools</a></li>
			<li class=""><a href="http://www.anc.org/software/uimautils/" alt="UIMA Tools">UIMA Tools</a></li>
		</ul>
	</div>
	<%-- page code --%>
	<wp:wrapper>
	<wp:section title="ANC2Go">
	<span class="instruction">Select a corpus and its directories:</span>
	<div class="tool-section">
		<div id="side-links">
			<h2>Corpora:</h2>
			<ul>
				<g:each in="${corpora.asList()}" var="currentCorpus">					
					<anc:content title="${corpus.name}" id="corpus-${corpus.name}" >
					<g:if test="${currentCorpus.name.equals(corpus.name)}">
						<g:set var="currentClass" value="current_page_item"/>
					</g:if>
					<g:else>
						<g:set var="currentClass" value=""/>
					</g:else>
					<li class="page_item ${currentClass}"><a href="/ANC2Go/${currentCorpus.name}">${currentCorpus.name}</a></li>       				
					</anc:content>
				</g:each>
			</ul>
		</div>
		<div id="right-panel">
			<h2>Directories:</h2>
			<div id="options">
				<table class="list" id="corpDirTable">
					<g:each in="${corpus.directories.asList().sort()}" var="dir">
					<tr>
						<td>
							<g:checkBox
							id="DIR_${corpus.cid}_${dir.did}" 
							name="F_${corpus.cid}_${dir.did}_dir" 
							class="DIR_${corpus.cid}" 
							checked="false"/>
						</td>
						<td><label for="DIR_${corpus.cid}_${dir.did}"> ${dir.textClass} - ${dir.name}</label></td>
					</tr>
					</g:each>
				</table>
			</div>
			<div id="select-buttons">
				<button type="button" onClick="selectAll(true)" class="btn btn-default">Select All</button>
				<button type="button" onClick="selectAll(false)" class="btn btn-default">Clear All</button>
			</div>
		</div>
	</div>
	<span class="instruction">Select the annotations:</span>
	<div class="tool-section">
	<g:each var="descriptor" in="${descriptors}">
	<div class="annotation-section">
		<g:checkBox
		id="check_${descriptor.name}"
		checked="false"/>
		<label for="check_${descriptor.name}" class="annotation">${descriptor.name}</label>
		<div class="annotation-options">
		<g:each var="cd" in="${descriptor.corpora}">

				<%--<g:if test="${cd.notes}">
					<h3>Notes</h3>
					<g:each var="p" in="${cd.notes}">
					<p>${p}</p>
					</g:each>
				</g:if>--%>
				<g:if test="${cd.name==corpus.name}">
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

						

							<g:elseif
								test="${token.description.contains('Penn TreeBank Tokens')}">
								<%-- PENN TREEBANK --%>
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

						

						<g:elseif
						test="${token.description.contains('FrameNet Tokens')}">
						<%-- FRAMENET --%>
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
						</tr>
						</table>
					</g:each>
				</g:if>

		</g:each>
		</div>
	</div>
	</g:each>
	</div>
	<span class="instruction">Enter your email:</span>
	<div class="tool-section">
		<div id="email-field">
			<input id="email-input" type="email" class="form-control" placeholder="Email">
		</div>
		<div id="info-field">
			You will receive a download link once your selection has been processed.
		</div>
	</div>
	<div id="submit-div">
	<g:actionSubmit id="submit-button" value="Submit" action="options" class="btn btn-primary btn-lg"/>
	</div>
	</wp:section>
	</wp:wrapper>
	</wp:frame>
<script language="javascript" type="text/javascript" src=<g:resource dir="js" file="anc2go.js" /> ></script>
</body>
</html>

