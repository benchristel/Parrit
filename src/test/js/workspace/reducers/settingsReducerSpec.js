var deepFreeze = require('deep-freeze');
var settingsReducer = require('workspace/reducers/settingsReducer.js');

describe("settingsReducer", function() {

	it("sets up the default state", function() {
		var stateBefore = undefined;
		var stateAfter = {
            isNewPersonModalOpen: false,
            isNewSpaceModalOpen: false
		};

		expect(
			settingsReducer(stateBefore, {})
		).toEqual(stateAfter);
	});

	describe("actions", function() {
        describe("SET_NEW_PERSON_MODAL_OPEN", function() {
            it("should set isNewPersonModalOpen to the passed in value", function() {
                var stateBefore = {
                    isNewPersonModalOpen: false
                };

                var action = {
                    type: "SET_NEW_PERSON_MODAL_OPEN",
                    isOpen: true
                };

                var stateAfter = {
                    isNewPersonModalOpen: true
                };

                deepFreeze(stateBefore);
                deepFreeze(action);

                expect(
                    settingsReducer(stateBefore, action)
                ).toEqual(stateAfter);
            });
        });

        describe("SET_NEW_SPACE_MODAL_OPEN", function() {
            it("should set isNewSpaceModalOpen to the passed in value", function() {
                var stateBefore = {
                    isNewSpaceModalOpen: false
                };

                var action = {
                    type: "SET_NEW_SPACE_MODAL_OPEN",
                    isOpen: true
                };

                var stateAfter = {
                    isNewSpaceModalOpen: true
                };

                deepFreeze(stateBefore);
                deepFreeze(action);

                expect(
                    settingsReducer(stateBefore, action)
                ).toEqual(stateAfter);
            });
        });
	});
});