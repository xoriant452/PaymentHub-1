
Important URLs

OpenShift Commands:

5.  oc new-app . --strategy=docker
6.  oc start-build java --from-dir .
7.  oc get svc/docker-registry -o yaml | grep clusterIP:
8.  oc delete all --selector app=centos
9.  oc adm policy add-scc-to-user anyuid -z default -n project-name
10. oc login -u system:admin
11. oc create -f nfs-pv.yaml
12. oc create -f nfs-claim.yaml
13. oc expose svc/centos --hostname centos.apps.10.20.14.183.xip.io

Output of exposed port:

ping  centos.apps.10.20.14.183.xip.io
PING centos.apps.10.20.14.183.xip.io (10.20.14.183) 56(84) bytes of data.
64 bytes from router.default.svc.cluster.local (10.20.14.183): icmp_seq=1 ttl=63 time=0.682 ms






