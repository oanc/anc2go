import org.anc.togo.*
import org.anc.togo.auth.*
import org.anc.togo.constants.*;

import org.anc.conf.*

class BootStrap {

	def springSecurityService
   
	def init = { servletContext ->
		Role userRole = Role.findByAuthority('ADMIN') ?: new Role(authority:'ADMIN').save()
		Role adminRole = Role.findByAuthority('USER') ?: new Role(authority:'USER').save()
		
      DB.init()
      println "Database initialization complete."
	}

	def destroy = {
	}

}
