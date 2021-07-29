# furnace-scheduling

This repository provides the implementation which was used to generate the computational results presented in the (
currently under revision) paper __*Scheduling heating tasks on parallel furnaces with setup times and conflicts*__
by <a href="https://orcid.org/0000-0001-8317-5751">Julia
Lange<img  src="https://orcid.org/sites/default/files/images/orcid_16x16.png" style="width:1em;margin-left:.3em;"></a>
, <a href="https://orcid.org/0000-0002-8763-2056">Philipp
Fath<img  src="https://orcid.org/sites/default/files/images/orcid_16x16.png" style="width:1em;margin-left:.3em;"></a>,
and <a href="https://orcid.org/0000-0002-8977-9414">David
Sayah<img  src="https://orcid.org/sites/default/files/images/orcid_16x16.png" style="width:1em;margin-left:.3em;"></a>.

## Optimization Tools

* __*jDecOR*__ ([0.2.0](https://maven.optimal-solution.org/service/rest/repository/browse/releases/org/optsol/jdecor/)) <br>
  This project is based on the [jdecor-pojo-template](https://github.com/OPTIMAL-SOLUTION-org/jdecor-pojo-template). __*jDecOR*__ framework helps to focus on the mathematical formulation and keeps boilerplate of mixed-integer programming at a minimum level.
* __*Google OR-Tools*__ ([9.0.9048](https://developers.google.com/optimization/support/release_notes))
* __*SCIP*__ ([7.0.1](https://www.scipopt.org/doc-7.0.1/html/))

## Prerequisites

* Java 11
* Maven 3
* Lombok
