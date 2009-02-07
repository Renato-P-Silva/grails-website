package org.grails.plugin

import grails.test.ControllerUnitTestCase

/*
 * author: Matthew Taylor
 */
class PluginControllerTests extends ControllerUnitTestCase {

    void testShowPlugin() {
        Plugin p = new Plugin(id:1, title:'plugin',body:'stuff')

        mockDomain(Plugin, [p])
        mockParams.id = 1

        def controller = new PluginController()
        def model = controller.show()

        assert p

        assertEquals p, model.plugin
    }

    void testCreatePluginGET() {
        mockRequest.method = 'GET'
        mockParams.title='my plugin'
        mockParams.description='stuff here'

        def controller = new PluginController()
        def model = controller.create()

        assert model.plugin
        assertEquals 'my plugin', model.plugin.title
        assertEquals 'stuff here', model.plugin.description
        assertEquals 'stuff here', model.plugin.body
    }

    void testCreatePluginValidationError() {
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockParams.description='stuff here'

        Plugin.metaClass.save = { -> null }

        def controller = new PluginController()
        def model = controller.create()

        assert model.plugin
        assertEquals 'my plugin', model.plugin.title
    }

    void testCreatePluginValidationSuccess() {
        mockDomain(Plugin)
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockParams.description='stuff here'
        def redirectParams = [:]

        PluginController.metaClass.redirect = { Map args -> redirectParams = args }
        Plugin.metaClass.save = { -> delegate }

        def controller = new PluginController()
        controller.create()

        assertEquals "/", redirectParams.uri
    }
}