
Important URLs

OpenShift Commands:

5.  oc new-app . --strategy=docker --name="batch-kafka2kafka-publisher"
6.  oc start-build batch-kafka2kafka-publisher  --from-dir .
7.  oc new-app . --strategy=docker --name="batch-kafka2kafka-subscriber"
8.  oc start-build batch-kafka2kafka-subscriber  --from-dir .
9.  oc start-build java --from-dir .
10. oc get svc/docker-registry -o yaml | grep clusterIP:
11. oc delete all --selector app=centos
12. oc adm policy add-scc-to-user anyuid -z default -n project-name
13. oc login -u system:admin
14. oc create -f nfs-pv.yaml
15. oc create -f nfs-claim.yaml
16. oc expose svc/centos --hostname centos.apps.10.20.14.183.xip.io

Output of exposed port:

ping  centos.apps.10.20.14.183.xip.io
PING centos.apps.10.20.14.183.xip.io (10.20.14.183) 56(84) bytes of data.
64 bytes from router.default.svc.cluster.local (10.20.14.183): icmp_seq=1 ttl=63 time=0.682 ms






