# avia-search
**avia-search** is an Android application that shows animation of searching for air tickets between two cities.

The animation slows down over time, mimicking the waiting for a server response.

When cities are placed in the same vertical hemisphere of the Earth, they are connected by a cubic Bézier curve.

|![Saint-Petersburg -> Vladivostok](/docs/saint-petersburg_vladivostok.gif)|![Jakarta -> Capetown](/docs/jakarta_capetown.gif)|
|---|---|
|Saint-Petersburg -> Vladivostok|Jakarta -> Capetown|

When cities are placed in the different vertical hemispheres of the Earth, they are connected by a linear Bézier curve.

|![Moscow -> Rio-De-Janeiro](/docs/moscow_rio-de-janeiro.gif)|![Tokyo -> Sydney](/docs/tokyo-sydney.gif)|
|---|---|
|Moscow -> Rio-De-Janeiro|Tokyo -> Sydney|