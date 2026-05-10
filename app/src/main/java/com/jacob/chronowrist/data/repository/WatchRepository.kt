package com.jacob.chronowrist.data.repository

import com.jacob.chronowrist.R
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchBadge
import com.jacob.chronowrist.data.model.WatchCategory
import com.jacob.chronowrist.data.model.WatchSpecs

object WatchRepository {

    val watches = listOf(
        Watch(
            id = 1,
            brand = "Aurum",
            name = "Classique Automatique",
            imageRes = R.drawable.watch1,
            price = 1290.0,
            category = WatchCategory.DRESS,
            badge = WatchBadge.NEW,
            rating = 5,
            reviewCount = 38,
            description = "A masterpiece of understated elegance. Hand-wound movement with 72-hour power reserve, encased in brushed 316L stainless steel with a domed sapphire crystal.",
            specs = WatchSpecs(
                movement = "Automatic ETA 2824",
                caseSize = "40mm Steel",
                crystal = "Sapphire",
                waterResistance = "50m"
            )
        ),
        Watch(
            id = 2,
            brand = "Nereus",
            name = "Abyss Diver 300",
            imageRes = R.drawable.watch2,
            price = 895.0,
            oldPrice = 1100.0,
            category = WatchCategory.DIVE,
            badge = WatchBadge.SALE,
            rating = 4,
            reviewCount = 62,
            description = "Built for the deep. A unidirectional bezel, helium escape valve, and lume plots that shine bright at 300 metres depth. Reliable and rugged.",
            specs = WatchSpecs(
                movement = "Seiko NH35A",
                caseSize = "42mm Titanium",
                crystal = "AR Sapphire",
                waterResistance = "300m"
            )
        ),
        Watch(
            id = 3,
            brand = "Vortex",
            name = "Chronos GT Racing",
            imageRes = R.drawable.watch3,
            price = 2150.0,
            category = WatchCategory.CHRONO,
            badge = WatchBadge.HOT,
            rating = 5,
            reviewCount = 21,
            description = "Inspired by the racetrack. A flyback chronograph with a tachymeter bezel and bi-compax subdials. Swiss Valjoux 7750 movement. Zero compromise.",
            specs = WatchSpecs(
                movement = "Valjoux 7750",
                caseSize = "44mm Black PVD",
                crystal = "Sapphire",
                waterResistance = "100m"
            )
        ),
        Watch(
            id = 4,
            brand = "Solaris",
            name = "Heritage Field",
            imageRes = R.drawable.watch4,
            price = 620.0,
            category = WatchCategory.SPORT,
            badge = null,
            rating = 4,
            reviewCount = 95,
            description = "Heritage Field draws from a tradition of military precision. Canvas strap, tritium lume, and a movement tested to MIL-SPEC standards.",
            specs = WatchSpecs(
                movement = "Miyota 9015",
                caseSize = "38mm Bronze",
                crystal = "Mineral",
                waterResistance = "100m"
            )
        ),
        Watch(
            id = 5,
            brand = "Luminos",
            name = "Moonphase Élite",
            imageRes = R.drawable.watch5,
            price = 3400.0,
            category = WatchCategory.DRESS,
            badge = WatchBadge.NEW,
            rating = 5,
            reviewCount = 14,
            description = "A grand complication in a slim 8.9mm case. The moonphase display is accurate to one day in 122 years. Alligator strap and deployment clasp.",
            specs = WatchSpecs(
                movement = "In-house Cal. L7",
                caseSize = "39mm Rose Gold",
                crystal = "Sapphire",
                waterResistance = "30m"
            )
        ),
        Watch(
            id = 6,
            brand = "Nereus",
            name = "Coastal GMT",
            imageRes = R.drawable.watch6,
            price = 1080.0,
            category = WatchCategory.SPORT,
            badge = null,
            rating = 4,
            reviewCount = 47,
            description = "Track two time zones simultaneously. 24-hour bi-directional bezel, Super-LumiNova indices, and a jubilee bracelet that wears beautifully day or night.",
            specs = WatchSpecs(
                movement = "NH34 GMT",
                caseSize = "41mm Steel",
                crystal = "Sapphire",
                waterResistance = "200m"
            )
        ),
        Watch(
            id = 7,
            brand = "Casio",
            name = "GT 890",
            imageRes = R.drawable.watch7,
            price = 502.0,
            category = WatchCategory.SPORT,
            badge = null,
            rating = 3,
            reviewCount = 51,
            description = "Track two time zones simultaneously. 24-hour bi-directional bezel, Super-LumiNova indices, and a jubilee bracelet that wears beautifully day or night.",
            specs = WatchSpecs(
                movement = "NH34 GMT",
                caseSize = "41mm Steel",
                crystal = "Sapphire",
                waterResistance = "200m"
            )
        )
    )
}
