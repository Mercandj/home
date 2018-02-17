package com.mercandalli.core.train

class TrainConst {

    /**
     * https://api-ratp.pierre-grimaud.fr/v3/documentation#get--schedules-{type}-{code}-{station}-{way}
     */
    companion object {
        var TRAFFIC_RER_A = "https://api-ratp.pierre-grimaud.fr/v3/traffic/rers/a"
        var TRAFFIC_RER_D = "https://api-ratp.pierre-grimaud.fr/v3/traffic/rers/d"
        var TRAFFIC_METRO_9 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/9"
        var TRAFFIC_METRO_14 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/14"
        var TRAFFIC_STATIONS_9 = "https://api-ratp.pierre-grimaud.fr/v3/stations/metros/9"
        var BILLANCOURT_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/metros/9/billancourt/A"
        var YERRES_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/metros/9/billancourt/A"
    }

}