C
C  include bessel k function and suport functions
C
      INCLUDE 'zbsubs.f'
      INCLUDE 'machcon.f'
C
C     
      SUBROUTINE BESSELK(ZR, ZI, REAL0, IMG0, REAL1, IMG1, IERR)
      DOUBLE PRECISION ZR, ZI, CYR, CYI, FNU, REAL0, IMG0, REAL1, IMG1
      INTEGER KODE, N, IERR
      DIMENSION CYR(2), CYI(2)
      FNU = 0.0
      KODE = 1
      N = 2
C
      CALL ZBESK(ZR, ZI, FNU, KODE, N, CYR, CYI, NZ, IERR)
      REAL0 = CYR(1)
      REAL1 = CYR(2)
      IMG0 = CYI(1)
      IMG1 =CYI(2) 
C      
      RETURN
      END