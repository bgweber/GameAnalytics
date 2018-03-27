# A Full-Managed Game Analytics Architecture

A lightweight implementation of the GCP game analytics [reference architecture](https://cloud.google.com/solutions/mobile/mobile-gaming-analysis-telemetry). While the implementation is minimal, this architecture can levelage the auto-scaling feature of DataFlow to scale to a massive volume of events. This project defines a data pipeline that consumes events from PubSub, and writes the events to BigQuery and AVRO. The AVRO files on google storage represent a data lake that can be used in further ETL steps, such as splitting out the raw events into schematized events. The table on BigQuery can be used to build view of specific events, or also used in downstream ETL processes. 

This project contains two directories. The events directory contains sample code for sending game events to a PubSub topic on GCP, and the arch directory contains a dataflow pipeline for storing these events to BigQuery and Google Storage as AVRO. The complete graph for this lightweight implementation is shown below:

![Architecture](/Arch.png "Analytics Architecture")

This native implementation sends all events in a JSON format, and does not split up different types of events into different tables. Example records in BigQuery are shown below:

![Example Events](/Events.png "Example Events")
