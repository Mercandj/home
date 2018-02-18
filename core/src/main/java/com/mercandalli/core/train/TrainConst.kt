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
        private const val YERRES_UIC = "87682112"
        private const val MELUN_UIC = "87682005"
        private const val GDL_UIC_1 = "87686006"
        private const val GDL_UIC_2 = "87758581"
        private const val GOUSSAINVILLE_UIC = "87276246"
        private const val MALESHERBES_UIC = "87684415"
        private const val MONTEREAU_UIC = "87682302"
        private const val CORBEIL_ESSONNES_UIC = "87681007"
        private const val ORRY_LA_VILLE_COYE_LA_FORET_UIC = "87276279"
        private const val MONTARGIS_UIC = "87684001"

        const val SNCF_SCHEDULES_YERRES = "http://api.transilien.com/gare/$YERRES_UIC/depart/"
        const val SNCF_SCHEDULES_GDL = "http://api.transilien.com/gare/$GDL_UIC_1/depart/"

        fun trainUicToName(uic: String): String {
            return when (uic) {
                TrainConst.MELUN_UIC -> "Melun"
                TrainConst.YERRES_UIC -> "Yerres"
                TrainConst.GDL_UIC_1 -> "Gare de lyon 1"
                TrainConst.GDL_UIC_2 -> "Gare de lyon 2"
                TrainConst.GOUSSAINVILLE_UIC -> "Goussainville"
                TrainConst.MALESHERBES_UIC -> "Malesherbes"
                TrainConst.MONTEREAU_UIC -> "Montereau"
                TrainConst.CORBEIL_ESSONNES_UIC -> "Corbeil Essonnes"
                TrainConst.ORRY_LA_VILLE_COYE_LA_FORET_UIC -> "Orry la Ville Coye la Forêt"
                TrainConst.MONTARGIS_UIC -> "Montargis"
                else -> "Term: " + uic
            }
        }
    }
}