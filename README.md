# GameAnalytics

A lightweight implementation of the GCP game analytics [reference architecture](https://cloud.google.com/solutions/mobile/mobile-gaming-analysis-telemetry).

This project contains two directories. The events directory contains sample code for sending game events to a PubSub topic on GCP, and the arch directory contains a dataflow pipeline for storing these events to BigQuery and Google Storage as AVRO. The complete graph for this lightweight implementation is shown below:

![Architecture](/Arch.png "Analytics Architecture")

This native implementation sends all events in a JSON format, and does not split up different types of events into different tables. Example records in BigQuery are shown below:

![Example Events](/Events.png "Example Events")
