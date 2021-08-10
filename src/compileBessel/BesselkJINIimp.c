#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <jni.h>
#include "edu_oa_curvature_bessel_BesselkJINI.h"

/* f77 compilers tend to append the underscore. If the f77 name already
   has one underscore, g77 will add *two* underscores at the end. Define
   F77_ADD_UNDERSCORE in the Makefile. */

#ifndef F77_ADD_UNDERSCORE
# define F77_ADD_UNDERSCORE 1
#endif

#if F77_ADD_UNDERSCORE
# define F77_FUNCTION(f) f##_
# define F77_FUNCTION2(f) f##__
#else
# define F77_FUNCTION(f) f
# define F77_FUNCTION2(f) f
#endif

extern void F77_FUNCTION(besselk) (double *zr, double *zi, double *real0, double *img0, double *real1, double *img1, int *ierr);

JNIEXPORT jdoubleArray JNICALL 
Java_edu_oa_curvature_bessel_BesselkJINI_besselk (JNIEnv *env, jobject obj, jdouble zreal, jdouble zimg) {
  double zr, zi, real0, img0, real1, img1;
  int ierr;
  zr = zreal; 
  zi = zimg;
  
  F77_FUNCTION(besselk) (&zr, &zi, &real0, &img0, &real1, &img1, &ierr);
  
  /*
  printf ("zr    %f\n", zr);
  printf ("zi    %f\n", zi);
  printf ("real0 %f\n", real0);
  printf ("img0  %f\n", img0);
  printf ("real1 %f\n", real1);
  printf ("img1  %f\n", img1);
  printf ("ierr  %d\n", ierr);
  */
  
  double errorRep = ierr;
  
  jdoubleArray array = (*env)->NewDoubleArray(env, 5);
  (*env)->SetDoubleArrayRegion(env, array, 0, 1, &real0);
  (*env)->SetDoubleArrayRegion(env, array, 1, 1, &img0);
  (*env)->SetDoubleArrayRegion(env, array, 2, 1, &real1);
  (*env)->SetDoubleArrayRegion(env, array, 3, 1, &img1);
  (*env)->SetDoubleArrayRegion(env, array, 4, 1, &errorRep);
  return array;
}
