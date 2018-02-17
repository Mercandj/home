package com.mercandalli.core.train

class TrainConst {

    /**
     * https://api-ratp.pierre-grimaud.fr/v3/documentation#get--schedules-{type}-{code}-{station}-{way}
     */
    companion object {
        const val TRAFFIC_RER_A = "https://api-ratp.pierre-grimaud.fr/v3/traffic/rers/a"
        const val TRAFFIC_RER_D = "https://api-ratp.pierre-grimaud.fr/v3/traffic/rers/d"
        const val TRAFFIC_METRO_9 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/9"
        const val TRAFFIC_METRO_14 = "https://api-ratp.pierre-grimaud.fr/v3/traffic/metros/14"
        const val TRAFFIC_STATIONS_9 = "https://api-ratp.pierre-grimaud.fr/v3/stations/metros/9"
        const val SCHEDULES_BILLANCOURT_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/metros/9/billancourt/A"
        const val SCHEDULES_YERRES_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/rers/d/yerres/A"
        const val SCHEDULES_GARE_DE_LYON_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/rers/a/gare+de+lyon/R"
        const val SCHEDULES_GARE_DE_LYON_D = "https://api-ratp.pierre-grimaud.fr/v3/schedules/rers/d/gare+de+lyon/R"
        const val SCHEDULES_BOISSY_A = "https://api-ratp.pierre-grimaud.fr/v3/schedules/rers/a/boissy+saint+leger/A"

        /**
         * From here
         * https://ressources.data.sncf.com/explore/dataset/sncf-gares-et-arrets-transilien-ile-de-france/?sort=libelle
         *
         * https://blogs.infinitesquare.com/posts/iot/afficher-les-prochains-depart-de-ma-gare-rersncf-sur-mon-microsoft-band
         */
        const val YERRES_UIC = "87682112"
    }

}