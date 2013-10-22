package org.anc.togo

import org.anc.togo.db.Processor;

class ProcessorController {
   
   def scaffold = Processor
   
   def nltk = {}
   
   def xml = {}
   
//    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
//
//    def index = {
//        redirect(action: "list", params: params)
//    }
//
//    def list = {
//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [processorInstanceList: Processor.list(params), processorInstanceTotal: Processor.count()]
//    }
//
//    def create = {
//        def processorInstance = new Processor()
//        processorInstance.properties = params
//        return [processorInstance: processorInstance]
//    }
//
//    def save = {
//        def processorInstance = new Processor(params)
//        if (processorInstance.save(flush: true)) {
//            flash.message = "${message(code: 'default.created.message', args: [message(code: 'processor.label', default: 'Processor'), processorInstance.id])}"
//            redirect(action: "show", id: processorInstance.id)
//        }
//        else {
//            render(view: "create", model: [processorInstance: processorInstance])
//        }
//    }
//
//    def show = {
//        def processorInstance = Processor.get(params.id)
//        if (!processorInstance) {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//            redirect(action: "list")
//        }
//        else {
//            [processorInstance: processorInstance]
//        }
//    }
//
//    def edit = {
//        def processorInstance = Processor.get(params.id)
//        if (!processorInstance) {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//            redirect(action: "list")
//        }
//        else {
//            return [processorInstance: processorInstance]
//        }
//    }
//
//    def update = {
//        def processorInstance = Processor.get(params.id)
//        if (processorInstance) {
//            if (params.version) {
//                def version = params.version.toLong()
//                if (processorInstance.version > version) {
//                    
//                    processorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'processor.label', default: 'Processor')] as Object[], "Another user has updated this Processor while you were editing")
//                    render(view: "edit", model: [processorInstance: processorInstance])
//                    return
//                }
//            }
//            processorInstance.properties = params
//            if (!processorInstance.hasErrors() && processorInstance.save(flush: true)) {
//                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'processor.label', default: 'Processor'), processorInstance.id])}"
//                redirect(action: "show", id: processorInstance.id)
//            }
//            else {
//                render(view: "edit", model: [processorInstance: processorInstance])
//            }
//        }
//        else {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//            redirect(action: "list")
//        }
//    }
//
//    def delete = {
//        def processorInstance = Processor.get(params.id)
//        if (processorInstance) {
//            try {
//                processorInstance.delete(flush: true)
//                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//                redirect(action: "list")
//            }
//            catch (org.springframework.dao.DataIntegrityViolationException e) {
//                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//                redirect(action: "show", id: params.id)
//            }
//        }
//        else {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processor.label', default: 'Processor'), params.id])}"
//            redirect(action: "list")
//        }
//    }
}
