class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/" {
         view = "/application/index" 
         controller = "application" 
         action = "index"
		}
      "/submit" {
         view = "application/submit"
         controller = "application"
         action = "submit"
      }
      
      "/index"(view:"index")
		"500"(view:'/error')

	  "/$corpusName" {
	  	controller = "application"
        action = "index"
	  	constraints {
	  		//matches "oanc" or "masc"
	  		corpusName(matches:"(?i)(o|m)a(n|s)c")
	  	}

	  }
	}
}
