<html>
<head>
	<title>ANC2Go Multi</title>
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href=<g:resource dir="css" file="anc2go.css" /> />
	<link rel="stylesheet" type="text/css" href=<g:resource dir="css" file="bootstrap.min.css" /> />
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
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
	<g:form action="submit" method="post" params="[corpusName : corpusName, selectedProcessor : selectedProcessor]">
	<span class="instruction">1. Select a corpus and its directories:</span>
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
			<div class="white-panel" id="options">
			<ul id="corpDirTable">
				<g:each in="${corpus.directories.asList().sort()}" var="dir">
					<li><anc:checkbox
							id="DIR_${corpus.cid}_${dir.did}" 
							name="F_${corpus.cid}_${dir.did}_dir" 
							class="DIR_${corpus.cid}" 
							checked="false"/>
					<label for="DIR_${corpus.cid}_${dir.did}"> ${dir.textClass} - ${dir.name}</label>
					</li>
					</g:each>
			</ul>
			</div>
			<div id="select-buttons">
				<button type="button" onClick="toggleAll()" class="btn btn-default">Toggle all</button>
			</div>
		</div>
	</div>
	<span class="instruction">2. Select an output format:</span>
	<div class="tool-section">
		<ul id="annotation-tabs" class="nav nav-pills">
		<g:each var="descriptor" in="${descriptors}" status="i">
    	<g:if test="${i == 0}">
			<li class="active"><a href="#tab_${i}" data-toggle="tab">${descriptor.name}</a></li>
			<g:set var="selectedProcessor" value="${descriptor.name}"/>
		</g:if>
		<g:else>
			<li><a href="#tab_${i}" data-toggle="tab">${descriptor.name}</a></li>
		</g:else>
		</g:each>
		</ul>
	</div>
	<span class="instruction">3. Select a base tokenization, and check the annotation types:</span>
	<div class="tool-section">
		<div class="white-panel tab-content">
			<g:each var="descriptor" in="${descriptors}" status="j">
			 	<g:if test="${j == 0}">
				<div class="tab-pane active" id="tab_${j}">
				</g:if>
				<g:else>
				<div class="tab-pane" id="tab_${j}">
				</g:else>
				<g:each var="cd" in="${descriptor.corpora}">
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
										&nbsp ${token.description}
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
									&nbsp ${token.description}
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
							&nbsp ${token.description}
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
			</g:each>
		</div>
	</div>
	<span class="instruction">4. Enter your email addresss:</span>
	<div class="tool-section">
		<div id="email-field">
			<input id="email-input" type="email" class="form-control" placeholder="Email">
		</div>
		<div id="info-field">
			You will receive a download link once your selection has been processed.
		</div>
	</div>
	<div id="submit-div">
	<g:actionSubmit id="submit-button" value="Submit" action="submit" class="btn btn-primary btn-lg"/>
	</div>
	</g:form>
	</wp:section>
	</wp:wrapper>
	</wp:frame>
<script language="javascript" type="text/javascript" src=<g:resource dir="js" file="anc2go.js" /> ></script>
</body>
</html>

