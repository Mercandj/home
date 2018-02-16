package com.mercandalli.core.train

class TrainConst {

    /**
     * https://api-ratp.pierre-grimaud.fr/v3/documentation#get--schedules-{type}-{code}-{station}-{way}
     */
    companion object {
        var RER_D = "https://api-ratp.pierre-grimaud.fr/v3/traffic/rers/d"
        var METRO_9 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/9"
        var METRO_14 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/14"
        var STATIONS_9 = "https://api-ratp.pierre-grimaud.fr/v3/stations/metros/9"
        var BILLANCOURT_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/metros/9/billancourt/A"
        var YERRES_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/metros/9/billancourt/A"
    }

}