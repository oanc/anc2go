package org.anc.togo

import org.anc.togo.db.*

class JobController {

    def scaffold = Job
    
    def summarize = {
       [jobs:Job.list(), pending:JobRequest.list()]
    }
    
}
