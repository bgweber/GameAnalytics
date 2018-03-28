# A Fully-Managed Game Analytics Architecture

A lightweight implementation of the GCP game analytics [reference architecture](https://cloud.google.com/solutions/mobile/mobile-gaming-analysis-telemetry). While the implementation is minimal, this architecture can levelage the auto-scaling feature of DataFlow to scale to a massive volume of events. This project defines a data pipeline that consumes events from PubSub, and writes the events to BigQuery and AVRO. The AVRO files on Google Storage represent a data lake that can be used in further ETL steps, such as splitting out the raw events into schematized events. The table on BigQuery can be used to build views of specific events, or also used in downstream ETL processes. 

![Data Pipeline](/Pipeline.png "Data Pipeline")

This project contains two directories. The events directory contains sample code for sending game events to a PubSub topic on GCP, and the arch directory contains a dataflow pipeline for storing these events to BigQuery and Google Storage as AVRO. The complete graph for this lightweight implementation is shown below:

![Architecture](/Arch.png "Analytics Architecture")

This naive implementation sends all events in a JSON format, and does not split up different types of events into different tables. Example records in BigQuery are shown below:

![Example Events](/Events.png "Example Events")
