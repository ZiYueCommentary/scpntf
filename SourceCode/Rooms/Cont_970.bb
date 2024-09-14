
Function FillRoom_Cont_970(r.Rooms)
	Local it.Items
	Local i
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 1288.0 * RoomScale, 0, r\z, 270, r)
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 760.0 * RoomScale, 0, r\z, 270, r)
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z, 270, r)
	r\RoomDoors[3] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 270, r)
	r\RoomDoors[4] = CreateDoor(r\zone, r\x + 760.0 * RoomScale, 0, r\z, 270, r)
	r\RoomDoors[5] = CreateDoor(r\zone, r\x + 1288.0 * RoomScale, 0, r\z, 270, r)
	
	For i = 0 To 5
		MoveEntity r\RoomDoors[i]\buttons[0], 0,0,-8.0
		MoveEntity r\RoomDoors[i]\buttons[1], 0,0,-8.0
		r\RoomDoors[i]\AutoClose = False : r\RoomDoors[i]\open = False				
	Next
	
	it = CreateItem("Document SCP-939", "paper", r\x + 352.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 256.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle+4, 0
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("9V Battery", "bat", r\x + 352.0 * RoomScale, r\y + 112.0 * RoomScale, r\z + 448.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Empty Cup", "emptycup", r\x-672*RoomScale, 240*RoomScale, r\z+288.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Level 1 Key Card", "key1", r\x - 672.0 * RoomScale, r\y + 240.0 * RoomScale, r\z + 224.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Cont_970(e.Events)
	Local n.NPCs,it.Items,de.Decals,itt.ItemTemplates,it2.Items,src.Doors,dest.Doors
	Local temp%,x#,y#,z#,tex%
	Local i
	
	If PlayerRoom = e\room Then
		If e\EventState2 <= 0 Then
			e\room\RoomDoors[1]\locked = False
			e\room\RoomDoors[4]\locked = False
			
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtypeMTF Lor n\NPCtype = NPCtype173 Lor n\NPCtype = NPCtypeOldMan Then 
					If EntityDistanceSquared(Collider, n\obj)<PowTwo(8.0) Then 
						e\room\RoomDoors[1]\locked = True
						e\room\RoomDoors[4]\locked = True
					EndIf
				EndIf
			Next
			
			e\EventState2 = 70*5
		Else
			e\EventState2 = e\EventState2 - FPSfactor
		EndIf
		
		TFormPoint EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,e\room\obj
		LightVolume = TempLightVolume*Max(Abs(TFormedZ())/1024.0,0.4)
		
		temp = 0
		If TFormedX()>730 Then
			UpdateWorld()
			TFormPoint EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,e\room\obj
			
			For i = 1 To 2
				src.Doors = e\room\RoomDoors[i + 2]
				dest.Doors = e\room\RoomDoors[i]
				
				dest\open = src\open
				dest\openstate = src\openstate
				EntityParent(dest\obj, dest\frameobj) : EntityParent(src\obj, src\frameobj)
				EntityParent(dest\obj2, dest\frameobj) : EntityParent(src\obj2, src\frameobj)
				
				PositionEntity(dest\obj, EntityX(src\obj), EntityY(src\obj), EntityZ(src\obj))
				PositionEntity(dest\obj2, EntityX(src\obj2), EntityY(src\obj2), EntityZ(src\obj2))
				
				EntityParent(dest\obj, 0) : EntityParent(src\obj, 0)
				EntityParent(dest\obj2, 0) : EntityParent(src\obj2, 0)
				
				src\open = False
				src\openstate = 0.0
			Next	
			
			TFormPoint TFormedX()-1024, TFormedY(), TFormedZ(),e\room\obj,0
			PositionEntity(Collider, TFormedX(), EntityY(Collider), TFormedZ(), True)
			ResetEntity(Collider)
			
			temp = True
			
		ElseIf TFormedX()<-730
			UpdateWorld()
			TFormPoint EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,e\room\obj
			
			For i = 1 To 2
				src.Doors = e\room\RoomDoors[i]
				dest.Doors = e\room\RoomDoors[i + 2]
				
				dest\open = src\open
				dest\openstate = src\openstate
				EntityParent(dest\obj, dest\frameobj) : EntityParent(src\obj, src\frameobj)
				EntityParent(dest\obj2, dest\frameobj) : EntityParent(src\obj2, src\frameobj)
				
				PositionEntity(dest\obj, EntityX(src\obj), EntityY(src\obj), EntityZ(src\obj))
				PositionEntity(dest\obj2, EntityX(src\obj2), EntityY(src\obj2), EntityZ(src\obj2))
				
				EntityParent(dest\obj, 0) : EntityParent(src\obj, 0)
				EntityParent(dest\obj2, 0) : EntityParent(src\obj2, 0)
				
				src\open = False
				src\openstate = 0.0
			Next
			
			TFormPoint TFormedX()+1024, TFormedY(), TFormedZ(),e\room\obj,0
			PositionEntity(Collider, TFormedX(), EntityY(Collider), TFormedZ(), True)
			ResetEntity(Collider)
			
			temp = True
			
		EndIf
		
		If temp = True Then 
			
			e\EventState=e\EventState+1
			
			For it.Items = Each Items
				If EntityDistanceSquared(it\collider,Collider)<PowTwo(5.0) Then
					
					TFormPoint EntityX(it\collider),EntityY(it\collider),EntityZ(it\collider),0,e\room\obj
					x = TFormedX() : y = TFormedY() : z = TFormedZ()
					If TFormedX()>264 Then
						TFormPoint x-1024,y,z,e\room\obj,0
						PositionEntity it\collider, TFormedX(), TFormedY(), TFormedZ()
						ResetEntity it\collider
					ElseIf TFormedX()<-264
						TFormPoint x+1024,y,z,e\room\obj,0
						PositionEntity it\collider, TFormedX(), TFormedY(), TFormedZ()
						ResetEntity it\collider
					EndIf
					
				EndIf
			Next
			
			Select e\EventState 
				Case 2
					i = Rand(0, MaxItemAmount-1)
					If Inventory[i]<>Null Then RemoveItem(Inventory[i])								
				Case 5
					;Injuries = Injuries + 0.3
					DamageSPPlayer(5, True)
				Case 10
					de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(e\room\obj)+Cos(e\room\angle-90)*760*RoomScale, 0.0005, EntityZ(e\room\obj)+Sin(e\room\angle-90)*760*RoomScale,90,Rnd(360),0)
				Case 14
					For i = 0 To MaxItemAmount-1
						If Inventory[i]<> Null Then
							If Inventory[i]\itemtemplate\tempname = "paper" Then
								RemoveItem(Inventory[i])
								For itt.ItemTemplates = Each ItemTemplates
									If itt\tempname = "paper" And Rand(6)=1 Then
										Inventory[i] = CreateItem(itt\name, itt\tempname, 1,1,1)
										HideEntity Inventory[i]\collider
										Inventory[i]\Picked = True
										Exit
									EndIf
								Next
								Exit
							EndIf
						EndIf
					Next
				Case 18
					TFormPoint -344,176, 272, e\room\obj,0
					it.Items = CreateItem("Strange Note", "paper", TFormedX(), TFormedY(), TFormedZ())
					EntityType(it\collider, HIT_ITEM)
				Case 25
					e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj)+Cos(e\room\angle-90)*760*RoomScale, 0.35, EntityZ(e\room\obj)+Sin(e\room\angle-90)*760*RoomScale)
					RotateEntity e\room\NPC[0]\Collider, 0, e\room\angle-200, 0, True
					ChangeNPCTextureID(e\room\NPC[0], 1)
					SetAnimTime(e\room\NPC[0]\obj,80)
					e\room\NPC[0]\State=10
				Case 30
					i = Rand(0,MaxItemAmount-1)
					If Inventory[i]<>Null Then RemoveItem(Inventory[i])
					Inventory[i] = CreateItem("Strange Note", "paper", 1,1,1)
					HideEntity Inventory[i]\collider
					Inventory[i]\Picked = True
				Case 35
					For i = 0 To 3
						de.Decals = CreateDecal(DECAL_BLOODPOOL, e\room\x+Rnd(-2,2), 700*RoomScale, e\room\z+Rnd(-2,2), 270, Rand(360), 0)
						de\Size = 0.05 : de\SizeChange = 0.0005 : EntityAlpha(de\obj, 0.8) : UpdateDecals
					Next
				Case 40
					PlaySound_Strict(LoadTempSound("SFX\radio\franklin4.ogg"))
				Case 50
					e\room\NPC[1]=CreateNPC(NPCtypeGuard, EntityX(e\room\obj)+Cos(e\room\angle+90)*600*RoomScale, 0.35, EntityZ(e\room\obj)+Sin(e\room\angle+90)*600*RoomScale)
					e\room\NPC[1]\State=7
				Case 52
					If e\room\NPC[1] <> Null Then
						RemoveNPC(e\room\NPC[1])
						e\room\NPC[1]=Null
					EndIf
				Case 60
					If (Not HalloweenTex) And Curr173 <> Null Then
						HalloweenTex = True
						Local tex970 = LoadTexture_Strict("GFX\npcs\173\173h.pt", 1)
						EntityTexture Curr173\obj, tex970
						EntityTexture Curr173\obj2, tex970
						DeleteSingleTextureEntryFromCache tex970
					EndIf
			End Select
			
			If Rand(10)=1 Then
				temp = Rand(0,2)
				PlaySound_Strict(AmbientSFX[temp * 15 + Rand(0,AmbientSFXAmount[temp]-1)])
			EndIf
			
			PositionEntity(Camera, EntityX(Collider), EntityY(Camera), EntityZ(Collider), True)
			CaptureWorld()
		Else
			If e\room\NPC[0] <> Null Then
				If EntityDistanceSquared(Collider, e\room\NPC[0]\Collider)<PowTwo(3.0) Then
					If EntityInView(e\room\NPC[0]\obj, Camera) Then
						CurrCameraZoom = (Sin(Float(MilliSecs())/20.0)+1.0)*15.0
						HeartBeatVolume = Max(CurveValue(0.3, HeartBeatVolume, 2.0), HeartBeatVolume)
						HeartBeatRate = Max(HeartBeatRate, 120)
					EndIf
				EndIf
			EndIf
			
			If e\room\NPC[1] <> Null Then
				PointEntity e\room\NPC[1]\obj, Collider
				RotateEntity e\room\NPC[1]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[1]\obj),EntityYaw(e\room\NPC[1]\Collider),35),0
			EndIf
			
			For it.Items = Each Items
				If (it\Dropped=1 And Abs(TFormedX())<264) Lor it\Dropped=-1 Then
					DebugLog "dropping/picking: "+it\Dropped+" - "+EntityX(it\collider)+", "+EntityY(it\collider)+", "+EntityZ(it\collider)
					
					TFormPoint EntityX(it\collider),EntityY(it\collider),EntityZ(it\collider),0,e\room\obj
					x = TFormedX() : y = TFormedY() : z = TFormedZ()
					
					If it\Dropped=1 Then
						For i = -1 To 1 Step 2
							TFormPoint x+1024*i,y,z,e\room\obj,0
							it2.Items = CreateItem(it\name, it\itemtemplate\tempname, TFormedX(), EntityY(it\collider), TFormedZ())
							RotateEntity(it2\collider, EntityPitch(it\collider),EntityYaw(it\collider),0)
							EntityType(it2\collider, HIT_ITEM)
						Next
					Else
						For it2.Items = Each Items
							If it2<>it And it2\dist < 15.0 Then
								
								TFormPoint EntityX(it2\collider),EntityY(it2\collider),EntityZ(it2\collider),0,e\room\obj
								DebugLog TFormedZ()+" - "+z
								
								If TFormedZ()=z Then RemoveItem(it2) : DebugLog "item removed"									
							EndIf
						Next
					EndIf
					
					Exit
				EndIf
			Next					
		EndIf
		
	EndIf
	
	
	If e\EventState > 26 Then
		If Abs(EntityX(Collider)-e\room\x)<8.0 Then
			If Abs(EntityZ(Collider)-e\room\z)<8.0 Then
				If e\Sound = 0 Then
					e\Sound = LoadSound_Strict("SFX\SCP\970\Corpse.ogg")
				EndIf
				e\SoundCHN = LoopSound2(e\Sound, e\SoundCHN, Camera, e\room\NPC[0]\obj)
				If e\EventState < 30 Then
					LightVolume = TempLightVolume*0.4
				ElseIf e\EventState > 60
					AnimateNPC(e\room\NPC[0], 80, 61, -0.02, False)
					
					e\room\NPC[0]\DropSpeed = 0
					y = CurveValue(1.5+Sin(Float(MilliSecs())/20.0)*0.1,EntityY(e\room\NPC[0]\Collider),50.0)
					
					PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\NPC[0]\Collider),y,EntityZ(e\room\NPC[0]\Collider)
					TurnEntity e\room\NPC[0]\Collider,0,0.1*FPSfactor,0
				EndIf 								
			EndIf
			
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D