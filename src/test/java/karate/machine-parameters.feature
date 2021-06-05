Feature: Test machine parameters API

Background:
  * url baseUrl

  Scenario: Add machine parameters
    Given path 'parameters'
    And request '{  "machineKey": "embosser", "parameters": {"core_diameter": 3, "speed": 20 }}'
    And header content-type = "application/json"
    When method post
    Then status 201
    And header accept = "application/json"
    And match response == [{ machine: "embosser", parameters: [ { name: "core_diameter", value: 3.0, updatedAt: #present}, { name: "speed", value: 20.0, updatedAt: #present }]}]

  Scenario: Get lastest parameters
    Given path 'parameters'
    When method get
    Then status 200
    And header accept = "application/json"

  Scenario: Get machine statistics
    Given path 'parameters/statistics'
    When method get
    Then status 200
    And header accept = "application/json"